package edu.pjatk.planista.shared.kernel.dto;

import java.time.Instant;

public record ExecutionResponse(
        Long id,
        Long quantity,
        Long timeInSeconds,
        Instant createdAt,
        Instant updatedAt,
        ProcessResponse process
) {}