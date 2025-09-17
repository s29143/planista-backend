package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CompanyStatusRepository extends JpaRepository<CompanyStatus, Long> {
    Optional<CompanyStatus> findByNameIgnoreCase(String name);
}
