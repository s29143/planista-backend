package edu.pjatk.planista.contact.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactRequest(
        @NotBlank(message = "validation.required")
        @Size(min = 1, max = 50, message = "validation.size")
        String firstName,

        @NotBlank(message = "validation.required")
        @Size(min = 1, max = 50, message = "validation.size")
        String lastName,

        @Size(max = 255, message = "validation.size")
        String jobTitle,

        @Size(max = 50, message = "validation.size")
        String phoneNumber,

        @Size(max = 50, message = "validation.size")
        String mobileNumber,

        @Email(message = "{validation.email}")
        @Size(max = 320, message = "validation.size")
        String email,

        boolean phoneAgreement,
        boolean emailAgreement,

        @NotNull(message = "validation.required")
        Long userId,

        Long companyId,
        Long statusId
) {
}
