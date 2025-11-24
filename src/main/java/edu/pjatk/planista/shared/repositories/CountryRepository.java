package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.Country;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "countries"
)
public interface CountryRepository extends DictItemRepository<Country> {
}
