package edu.pjatk.planista.process.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record ProcessRequest(
        @Min(value = 0, message = "validation.min")
        Long quantity,
        @NotNull
        @Min(value = 1, message = "validation.min")
        Long plannedTimeSeconds,
        @NotNull(message = "validation.required")
        Long orderId,
        Long workstationId,
        Long technologyId,
        Long statusId
) {
}
