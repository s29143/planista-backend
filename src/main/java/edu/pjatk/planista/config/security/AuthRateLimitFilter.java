package edu.pjatk.planista.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

public class AuthRateLimitFilter extends OncePerRequestFilter {

    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final String REFRESH_PATH = "/api/v1/auth/refresh";

    private final ObjectMapper objectMapper;

    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .maximumSize(100_000)
            .expireAfterAccess(Duration.ofMinutes(30))
            .build();

    private final Bandwidth loginLimit = Bandwidth.builder()
            .capacity(5)
            .refillGreedy(5, Duration.ofMinutes(1))
            .build();

    private final Bandwidth refreshLimit = Bandwidth.builder()
            .capacity(30)
            .refillGreedy(30, Duration.ofMinutes(1))
            .build();

    public AuthRateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (!HttpMethod.POST.matches(method)) return true;

        return !(LOGIN_PATH.equals(path) || REFRESH_PATH.equals(path));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean isLogin = LOGIN_PATH.equals(path);
        boolean isRefresh = REFRESH_PATH.equals(path);

        HttpServletRequest reqToUse = request;
        if (isLogin && isJson(request)) {
            reqToUse = new CachedBodyHttpServletRequest(request);
        }

        String key = resolveKey(reqToUse, path);

        Bucket bucket = buckets.get(key, k ->
                Bucket.builder()
                        .addLimit(isLogin ? loginLimit : refreshLimit)
                        .build()
        );

        var probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            long limit = isLogin ? loginLimit.getCapacity() : refreshLimit.getCapacity();
            response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
            response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(reqToUse, response);
            return;
        }

        long waitSeconds = Math.max(1, probe.getNanosToWaitForRefill() / 1_000_000_000L);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(waitSeconds));
        response.setContentType("application/problem+json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("""
                {
                  "type": "about:blank",
                  "title": "Too many requests",
                  "status": 429,
                  "detail": "Rate limit exceeded. Try again later."
                }
                """);
    }

    private String resolveKey(HttpServletRequest request, String path) {
        String ip = clientIp(request);

        if (LOGIN_PATH.equals(path)) {
            String username = extractUsernameFromJsonBody(request);
            if (username == null || username.isBlank()) username = "unknown";
            return "login:" + ip + ":" + username.toLowerCase();
        }

        return "refresh:" + ip;
    }

    private String extractUsernameFromJsonBody(HttpServletRequest request) {
        if (!(request instanceof CachedBodyHttpServletRequest cached)) return null;

        try {
            byte[] body = cached.getCachedBody();
            if (body.length == 0) return null;

            JsonNode node = objectMapper.readTree(body);

            JsonNode usernameNode = node.get("username");
            if (usernameNode == null || usernameNode.isNull()) return null;

            String username = usernameNode.asText(null);
            if (username == null) return null;

            username = username.trim();
            return username.isEmpty() ? null : username;

        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean isJson(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) return false;
        return contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE);
    }

    private String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final byte[] cachedBody;

        CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.cachedBody = readAllBytes(request.getInputStream());
        }

        byte[] getCachedBody() {
            return cachedBody;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream bais = new ByteArrayInputStream(cachedBody);

            return new ServletInputStream() {
                @Override
                public int read() {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return bais.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // no-op
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }

        private static byte[] readAllBytes(InputStream is) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
}
