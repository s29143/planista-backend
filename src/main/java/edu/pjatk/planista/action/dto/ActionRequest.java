package edu.pjatk.planista.action.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ActionRequest(
        @NotNull(message = "validation.required")
        LocalDate date,

        String text,

        boolean done,

        boolean prior,

        boolean reminder,

        @NotNull(message = "validation.required")
        Long userId,
        Long companyId,
        Long contactId,
        Long typeId
) {
}
