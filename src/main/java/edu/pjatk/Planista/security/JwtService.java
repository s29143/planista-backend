package edu.pjatk.Planista.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-expiration}") long accessExpMs,
            @Value("${app.jwt.refresh-expiration}") long refreshExpMs
    ) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException e) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("app.jwt.secret must be >= 32 bytes for HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpMs = accessExpMs;
        this.refreshExpMs = refreshExpMs;
    }

    public String generateAccessToken(String username) {
        return generateAccessToken(username, Map.of());
    }

    public String generateAccessToken(String username, Map<String, Object> claims) {
        return buildToken(username, claims, accessExpMs, "ACCESS");
    }

    public boolean isAccessToken(String token) {
        return "ACCESS".equalsIgnoreCase(getTokenType(token));
    }

    public String generateRefreshToken(String username) {
        String jti = Jti.newId();
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + refreshExpMs))
                .claim("typ", "REFRESH")
                .id(jti)
                .signWith(signingKey)
                .compact();
    }

    public String extractJti(String token) {
        return extractClaim(token, Claims::getId);
    }
    public boolean isRefreshToken(String token) {
        return "REFRESH".equalsIgnoreCase(getTokenType(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            return expectedUsername.equals(extractUsername(token)) && !isExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> extractor) {
        return extractor.apply(parseAllClaims(token));
    }

    private String buildToken(String username, Map<String, Object> claims, long ttlMs, String type) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttlMs))
                .claim("typ", type)
                .signWith(signingKey)
                .compact();
    }

    private String getTokenType(String token) {
        return extractClaim(token, c -> c.get("typ", String.class));
    }

    private boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public Instant extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
