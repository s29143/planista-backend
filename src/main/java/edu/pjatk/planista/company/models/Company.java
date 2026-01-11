package edu.pjatk.planista.company.models;

import edu.pjatk.planista.shared.models.AppUser;
import edu.pjatk.planista.config.common.Auditable;
import edu.pjatk.planista.shared.models.Country;
import edu.pjatk.planista.shared.models.District;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Company extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String shortName;

    @Column(nullable = false, unique = true, length = 512)
    private String fullName;

    @Column(length = 10)
    private String nip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private AppUser user;

    @Column(length = 6)
    private String postalCode;

    private String street;

    @Column(length = 5)
    private String houseNumber;

    @Column(length = 5)
    private String apartmentNumber;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 320)
    private String email;

    @Column(length = 1024)
    private String wwwSite;

    @Column
    @Lob
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private CompanyAcquired acquired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private CompanyStatus status;

    public String getName() {
        return fullName + " (" + nip + ")";
    }
}
