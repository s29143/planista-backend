package edu.pjatk.planista.users.dto;

public record UserRequest(
        Long id,
        String username,
        String firstname,
        String lastname
) {
}
