package edu.pjatk.planista.execution.repositories;

import edu.pjatk.planista.execution.models.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long>, JpaSpecificationExecutor<Execution> {
}
