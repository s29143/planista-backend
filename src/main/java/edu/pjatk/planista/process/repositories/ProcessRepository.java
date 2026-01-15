package edu.pjatk.planista.process.repositories;

import edu.pjatk.planista.process.models.Process;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    @Query("""
    select p from Process p
    where p.dateFrom is not null
      and p.dateFrom <= :to
      and (p.dateTo is null or p.dateTo >= :from)
""")
    List<Process> findForGantt(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

}
