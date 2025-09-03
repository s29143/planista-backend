package edu.pjatk.planista.security;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private static final String SECRET = Base64.getEncoder()
            .encodeToString("super-secret-key-which-is-long-enough-32bytes!!!"
                    .getBytes(StandardCharsets.UTF_8));

    @Test
    void shouldGenerateAndValidateAccessToken() {
        var jwt = new JwtService(SECRET, Duration.of(60_000, ChronoUnit.MILLIS), Duration.of(1_200_000, ChronoUnit.MILLIS));
        var token = jwt.generateAccessToken("alice");

        assertThat(token).isNotBlank();
        assertThat(jwt.isAccessToken(token)).isTrue();
        assertThat(jwt.extractUsername(token)).isEqualTo("alice");
        assertThat(jwt.isTokenValid(token, "alice")).isTrue();
    }

    @Test
    void shouldGenerateRefreshWithJtiAndType() {
        var jwt = new JwtService(SECRET, Duration.of(60_000, ChronoUnit.MILLIS), Duration.of(1_200_000, ChronoUnit.MILLIS));
        var token = jwt.generateRefreshToken("bob");

        assertThat(token).isNotBlank();
        assertThat(jwt.isRefreshToken(token)).isTrue();
        assertThat(jwt.extractUsername(token)).isEqualTo("bob");
        assertThat(jwt.extractExpiration(token)).isAfter(java.time.Instant.now());
        assertThat(jwt.isTokenValid(token, "bob")).isTrue();
    }

    @Test
    void expiredAccessTokenIsInvalid() {
        var jwt = new JwtService(SECRET, Duration.ZERO, Duration.of(1_200_000, ChronoUnit.MILLIS));
        var token = jwt.generateAccessToken("carol");
        assertThat(jwt.isTokenValid(token, "carol")).isFalse();
    }
}
