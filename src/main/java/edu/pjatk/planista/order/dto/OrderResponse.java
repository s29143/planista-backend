package edu.pjatk.planista.order.dto;

import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.contact.dto.ContactResponse;
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