package edu.pjatk.planista.auth.dto;

public record MeResponse (
        Long id,
        String username,
        String firstname,
        String lastname
) {}
