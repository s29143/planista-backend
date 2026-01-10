package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;

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
        DictItemDto user,
        CompanyResponse company,
        ContactResponse contact,
        DictItemDto type
) {}