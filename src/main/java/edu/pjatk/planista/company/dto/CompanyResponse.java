package edu.pjatk.planista.company.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Instant;

public record CompanyResponse(
        Long id,
        String shortName,
        String fullName,
        String nip,
        String postalCode,
        String street,
        String houseNumber,
        String apartmentNumber,
        String phoneNumber,
        String email,
        String wwwSite,
        String comments,
        Instant createdAt,
        Instant updatedAt,
        DictItemDto user,
        DictItemDto acquired,
        DictItemDto district,
        DictItemDto country,
        DictItemDto status
) {}
