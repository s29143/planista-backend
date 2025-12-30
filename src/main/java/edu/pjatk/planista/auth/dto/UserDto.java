package edu.pjatk.planista.auth.dto;

import edu.pjatk.planista.auth.UserRole;

public record UserDto(
    Long id,
    String username,
    String firstname,
    String lastname,
    UserRole role
) {
}
