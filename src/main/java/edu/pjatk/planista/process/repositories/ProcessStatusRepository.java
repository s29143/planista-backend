package edu.pjatk.planista.process.repositories;

import edu.pjatk.planista.process.models.ProcessStatus;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "process-statuses",
        collectionResourceRel = "process-statuses"
)
public interface ProcessStatusRepository extends DictItemRepository<ProcessStatus> {
}
