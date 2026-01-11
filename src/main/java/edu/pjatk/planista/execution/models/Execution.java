package edu.pjatk.planista.execution.models;

import edu.pjatk.planista.config.common.Auditable;
import edu.pjatk.planista.process.models.Process;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Execution extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    private Long timeInSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private Process process;
}
