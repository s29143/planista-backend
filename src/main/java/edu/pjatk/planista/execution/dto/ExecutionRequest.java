package edu.pjatk.planista.execution.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record ExecutionRequest(
        @Min(value = 0, message = "validation.min")
        Integer quantity,
        Duration time,
        @NotNull(message = "validation.required")
        Long processId
) {
}
