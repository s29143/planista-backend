package edu.pjatk.Planista.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
