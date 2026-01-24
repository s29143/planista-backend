package edu.pjatk.planista.integration.models;

import lombok.Data;

@Data
public class CompanyFromRegonDto {
    private String regon;
    private String nip;
    private String name;

    private String city;
    private String street;
    private String buildingNo;
    private String apartmentNo;
    private String postalCode;
    private String postOffice;
}