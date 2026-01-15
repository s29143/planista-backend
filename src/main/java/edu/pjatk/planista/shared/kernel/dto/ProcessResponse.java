package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Instant;
import java.time.LocalDate;

public record ProcessResponse(
        Long id,
        Long quantity,
        LocalDate dateFrom,
        LocalDate dateTo,
        Long plannedTimeSeconds,
        Instant createdAt,
        Instant updatedAt,
        DictItemDto order,
        DictItemDto technology,
        DictItemDto workstation,
        DictItemDto status
) {}