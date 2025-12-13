package edu.pjatk.planista.contact.dto;

import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Instant;

public record ContactResponse(
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
        CompanyResponse company,
        DictItemDto status
) {}
