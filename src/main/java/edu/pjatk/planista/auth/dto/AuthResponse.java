package edu.pjatk.planista.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
