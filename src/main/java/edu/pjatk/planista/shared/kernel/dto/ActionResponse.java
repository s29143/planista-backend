package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.dto.IdNameDto;

import java.time.Instant;
import java.time.LocalDate;

public record ActionResponse(
        Long id,
        LocalDate date,
        String text,
        boolean done,
        boolean prior,
        boolean reminder,
        Instant createdAt,
        Instant updatedAt,
        IdNameDto user,
        IdNameDto company,
        IdNameDto contact,
        DictItemDto type
) {}