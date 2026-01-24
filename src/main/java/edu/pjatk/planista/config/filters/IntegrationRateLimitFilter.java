package edu.pjatk.planista.config.filters;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import edu.pjatk.planista.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

public class IntegrationRateLimitFilter extends OncePerRequestFilter {

    private static final String INTEGRATIONS_PREFIX = "/api/v1/integrations";

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .maximumSize(100_000)
            .expireAfterAccess(Duration.ofMinutes(30))
            .build();

    private final Bandwidth readLimit = Bandwidth.builder()
            .capacity(120)
            .refillGreedy(120, Duration.ofMinutes(1))
            .build();

    private final Bandwidth writeLimit = Bandwidth.builder()
            .capacity(30)
            .refillGreedy(30, Duration.ofMinutes(1))
            .build();

    public IntegrationRateLimitFilter(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = Objects.requireNonNull(handlerExceptionResolver);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null || !path.startsWith(INTEGRATIONS_PREFIX);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String method = request.getMethod();
        boolean isWrite =
                HttpMethod.POST.matches(method) ||
                        HttpMethod.PUT.matches(method) ||
                        HttpMethod.PATCH.matches(method) ||
                        HttpMethod.DELETE.matches(method);

        Bandwidth limitToUse = isWrite ? writeLimit : readLimit;

        String provider = extractProvider(request.getRequestURI());
        if (provider == null || provider.isBlank()) provider = "unknown";

        String ip = clientIp(request);

        String key = (isWrite ? "integrations:write:" : "integrations:read:")
                + provider.toLowerCase() + ":" + ip;

        Bucket bucket = buckets.get(key, k -> Bucket.builder().addLimit(limitToUse).build());

        var probe = bucket.tryConsumeAndReturnRemaining(1);
        if (!probe.isConsumed()) {
            long limit = limitToUse.getCapacity();
            long waitSeconds = Math.max(1, (probe.getNanosToWaitForRefill() + 999_999_999L) / 1_000_000_000L);

            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    new RateLimitExceededException(waitSeconds, limit, 0)
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractProvider(String uri) {
        if (uri == null) return null;

        String rest = uri.startsWith(INTEGRATIONS_PREFIX)
                ? uri.substring(INTEGRATIONS_PREFIX.length())
                : "";

        if (rest.startsWith("/")) rest = rest.substring(1);
        if (rest.isBlank()) return null;

        int slash = rest.indexOf('/');
        return (slash == -1) ? rest : rest.substring(0, slash);
    }

    private String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
