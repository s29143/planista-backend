package edu.pjatk.planista.execution.dto;

import edu.pjatk.planista.process.dto.ProcessResponse;

import java.time.Duration;
import java.time.Instant;

public record ExecutionResponse(
        Long id,
        Long quantity,
        Long timeInSeconds,
        Instant createdAt,
        Instant updatedAt,
        ProcessResponse process
) {}