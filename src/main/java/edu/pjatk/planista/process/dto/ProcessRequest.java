package edu.pjatk.planista.process.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record ProcessRequest(
        @Min(value = 0, message = "validation.min")
        Integer quantity,
        Duration plannedTime,
        @NotNull(message = "validation.required")
        Long orderId,
        Long workstationId,
        Long technologyId,
        Long statusId
) {
}
