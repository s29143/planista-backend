package edu.pjatk.planista.process.repositories;

import edu.pjatk.planista.process.models.Technology;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "technologies",
        collectionResourceRel = "technologies"
)
public interface TechnologyRepository extends DictItemRepository<Technology> {
}
