package edu.pjatk.planista.execution.repositories;

import edu.pjatk.planista.execution.models.Execution;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long>, JpaSpecificationExecutor<Execution> {
    Page<Execution> findByProcessId(Long processId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "process"
    })
    @Override
    Page<Execution> findAll(@NonNull Pageable pageable);
}
