package edu.pjatk.planista.execution.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record ExecutionRequest(
        @Min(value = 1, message = "validation.min")
        Long quantity,
        @NotNull
        @Min(value = 1, message = "validation.min")
        Long timeInSeconds,
        @NotNull(message = "validation.required")
        Long processId
) {
}
