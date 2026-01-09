package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.Technology;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "technologies",
        collectionResourceRel = "technologies"
)
public interface TechnologyRepository extends DictItemRepository<Technology> {
}
