package edu.pjatk.planista.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
