package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.dto.IdNameDto;

import java.time.Instant;

public record ExecutionResponse(
        Long id,
        Long quantity,
        Long timeInSeconds,
        Instant createdAt,
        Instant updatedAt,
        IdNameDto process
) {}