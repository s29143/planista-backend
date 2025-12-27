package edu.pjatk.planista.contact.models;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Contact extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    private String jobTitle;

    private String phoneNumber;

    private String mobileNumber;

    @Column(length = 320)
    private String email;

    private boolean phoneAgreement;

    private boolean emailAgreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private ContactStatus status;
}
