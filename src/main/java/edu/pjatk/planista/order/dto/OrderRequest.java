package edu.pjatk.planista.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record OrderRequest(
        @NotBlank(message = "validation.required")
        @Size(min = 1, max = 50, message = "validation.size")
        String product,
        LocalDate dateFrom,
        LocalDate dateTo,
        @Min(value = 0, message = "validation.min")
        Integer quantity,

        Long companyId,
        Long contactId,
        Long statusId,
        Long typeId
) {
}
