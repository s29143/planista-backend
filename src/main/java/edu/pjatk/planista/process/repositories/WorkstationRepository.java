package edu.pjatk.planista.process.repositories;

import edu.pjatk.planista.process.models.Workstation;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "workstations",
        collectionResourceRel = "workstations"
)
public interface WorkstationRepository extends DictItemRepository<Workstation> {
}
