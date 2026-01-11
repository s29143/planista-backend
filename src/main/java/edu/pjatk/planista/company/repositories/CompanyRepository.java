package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.Company;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    @EntityGraph(attributePaths = {
            "user",
            "acquired",
            "district",
            "country",
            "status"
    })
    @Override
    Page<Company> findAll(Specification<Company> specification, @NonNull Pageable pageable);
}
