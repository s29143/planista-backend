package edu.pjatk.planista.process.models;

import edu.pjatk.planista.config.Auditable;
import edu.pjatk.planista.order.models.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Getter
@Setter
public class Process extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    private Duration plannedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private ProcessStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id")
    private Technology technology;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workstation_id")
    private Workstation workstation;
}
