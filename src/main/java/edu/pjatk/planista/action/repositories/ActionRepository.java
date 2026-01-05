package edu.pjatk.planista.action.repositories;

import edu.pjatk.planista.action.models.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long>, JpaSpecificationExecutor<Action> {
    Page<Action> findByCompanyId(Long companyId, Pageable pageable);
    Page<Action> findByContactId(Long contactId, Pageable pageable);
}
