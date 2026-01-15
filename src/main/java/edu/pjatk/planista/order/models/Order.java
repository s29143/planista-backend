package edu.pjatk.planista.order.models;

import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.config.common.Auditable;
import edu.pjatk.planista.contact.models.Contact;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(
        name = "commission",
        indexes = {
                @Index(name = "idx_order_date_from", columnList = "dateFrom"),
                @Index(name = "idx_order_date_to", columnList = "dateTo")
        }
)
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String product;

    private Integer quantity;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private OrderType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    public String getName() {
        return "#" + id + " " + product;
    }
}
