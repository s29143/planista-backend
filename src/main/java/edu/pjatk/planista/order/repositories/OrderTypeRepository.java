package edu.pjatk.planista.order.repositories;

import edu.pjatk.planista.order.models.OrderType;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "order-types",
        collectionResourceRel = "order-types"
)
public interface OrderTypeRepository extends DictItemRepository<OrderType> {
}
