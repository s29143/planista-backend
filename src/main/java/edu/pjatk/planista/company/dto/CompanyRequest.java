package edu.pjatk.planista.company.dto;

import jakarta.validation.constraints.*;

public record CompanyRequest(
        @NotBlank
        @Size(max = 255)
        String shortName,
        @NotBlank
        @Size(max = 512)
        String fullName,
        @Pattern(regexp = "\\d{10}")
        String nip,
        @Pattern(regexp = "\\d{2}-\\d{3}")
        String postalCode,
        @Size(max = 255)
        String street,
        @Size(max = 5)
        String houseNumber,
        @Size(max = 5)
        String apartmentNumber,
        @Size(max = 15)
        String phoneNumber,
        @Email
        String email,
        @Size(max = 1024)
        String wwwSite,
        @Size(max = 10_000)
        String comments,
        @NotNull
        Long userId,
        Long acquiredId,
        Long districtId,
        Long countryId,
        Long statusId
) {}
