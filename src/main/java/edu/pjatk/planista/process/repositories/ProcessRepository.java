package edu.pjatk.planista.process.repositories;

import edu.pjatk.planista.process.models.Process;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long>, JpaSpecificationExecutor<Process> {
    Page<Process> findByOrderId(Long orderId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "order",
            "technology",
            "status",
            "workstation"
    })
    @Override
    Page<Process> findAll(@NonNull Pageable pageable);
}
