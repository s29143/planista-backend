package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.Workstation;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "workstations",
        collectionResourceRel = "workstations"
)
public interface WorkstationRepository extends DictItemRepository<Workstation> {
}
