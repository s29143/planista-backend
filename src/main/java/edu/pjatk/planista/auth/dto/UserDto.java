package edu.pjatk.planista.auth.dto;

public record UserDto(
    Long id,
    String username,
    String firstname,
    String lastname
) {
}
