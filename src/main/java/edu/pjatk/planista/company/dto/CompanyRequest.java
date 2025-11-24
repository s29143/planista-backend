package edu.pjatk.planista.company.dto;

import jakarta.validation.constraints.*;

public record CompanyRequest(
        @NotBlank(message = "{validation.required}")
        @Size(max = 255, message = "{validation.size}")
        String shortName,
        @NotBlank(message = "{validation.required}")
        @Size(max = 512, message = "{validation.size}")
        String fullName,
        @Pattern(regexp = "\\d{10}")
        String nip,
        @Pattern(regexp = "\\d{2}-\\d{3}")
        String postalCode,
        @Size(max = 255, message = "{validation.size}")
        String street,
        @Size(max = 5, message = "{validation.size}")
        String houseNumber,
        @Size(max = 5, message = "{validation.size}")
        String apartmentNumber,
        @Size(max = 15, message = "{validation.size}")
        String phoneNumber,
        @Email
        String email,
        @Size(max = 1024, message = "{validation.size}")
        String wwwSite,
        @Size(max = 10_000, message = "{validation.size}")
        String comments,
        @NotNull
        Long userId,
        Long acquiredId,
        Long districtId,
        Long countryId,
        Long statusId
) {}
