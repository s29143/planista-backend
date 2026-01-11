package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Instant;

public record ProcessResponse(
        Long id,
        Long quantity,
        Long plannedTimeSeconds,
        Instant createdAt,
        Instant updatedAt,
        DictItemDto order,
        DictItemDto technology,
        DictItemDto workstation,
        DictItemDto status
) {}