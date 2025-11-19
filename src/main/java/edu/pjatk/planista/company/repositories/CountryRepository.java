package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.Country;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "countries"
)
public interface CountryRepository extends DictItemRepository<Country> {
}
