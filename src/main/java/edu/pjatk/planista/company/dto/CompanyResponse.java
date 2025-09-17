package edu.pjatk.planista.company.dto;

public record CompanyResponse(
        Long id,
        String shortName,
        String fullName,
        String nip,

        Long userId,
        Long acquiredId,
        Long districtId,
        Long countryId,
        Long statusId,

        String postalCode,
        String street,
        String houseNumber,
        String apartmentNumber,
        String phoneNumber,
        String email,
        String wwwSite,
        String comments
) {}
