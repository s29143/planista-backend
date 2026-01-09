package edu.pjatk.planista.action.repositories;

import edu.pjatk.planista.action.models.Action;
import edu.pjatk.planista.contact.models.Contact;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long>, JpaSpecificationExecutor<Action> {
    Page<Action> findByCompanyId(Long companyId, Pageable pageable);
    Page<Action> findByContactId(Long contactId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "user",
            "company",
            "contact",
            "type"
    })
    @Override
    Page<Action> findAll(Specification<Action> specification, @NonNull Pageable pageable);
}
