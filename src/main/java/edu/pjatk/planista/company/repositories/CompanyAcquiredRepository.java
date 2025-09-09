package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.CompanyAcquired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CompanyAcquiredRepository extends JpaRepository<CompanyAcquired, Long> {
    Optional<CompanyAcquired> findByNameIgnoreCase(String name);
}
