package edu.pjatk.planista.action.repositories;

import edu.pjatk.planista.action.models.ActionType;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "action-types",
        collectionResourceRel = "action-types"
)
public interface ActionTypeRepository extends DictItemRepository<ActionType> {
}
