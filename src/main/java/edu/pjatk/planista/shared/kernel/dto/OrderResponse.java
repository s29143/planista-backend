package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Instant;
import java.time.LocalDate;

public record OrderResponse(
        Long id,
        String product,
        LocalDate dateFrom,
        LocalDate dateTo,
        Long quantity,
        Instant createdAt,
        Instant updatedAt,
        DictItemDto status,
        CompanyResponse company,
        ContactResponse contact,
        DictItemDto type
) {}