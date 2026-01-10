package edu.pjatk.planista.shared.kernel.dto;

import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Instant;

public record ContactResponse(
        Long id,
        String firstName,
        String lastName,
        String jobTitle,
        String phoneNumber,
        String mobileNumber,
        String email,
        boolean phoneAgreement,
        boolean emailAgreement,
        Instant createdAt,
        Instant updatedAt,
        DictItemDto user,
        CompanyResponse company,
        DictItemDto status
) {}