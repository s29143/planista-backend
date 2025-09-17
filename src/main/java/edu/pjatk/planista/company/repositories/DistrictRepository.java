package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findByNameIgnoreCase(String name);
}
