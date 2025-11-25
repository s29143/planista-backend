package edu.pjatk.planista.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "{validation.required}")
        @Size(max = 40, message = "{validation.size}")
        String username,
        @NotBlank(message = "{validation.required}")
        @Size(max = 50, message = "{validation.size}")
        String firstname,
        @NotBlank(message = "{validation.required}")
        @Size(max = 50, message = "{validation.size}")
        String lastname,
        @Size(min = 8, max = 32, message = "{validation.size}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,32}$",
                message = "{validation.password}"
        )
        String password
) {
}
