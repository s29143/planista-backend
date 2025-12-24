package edu.pjatk.planista.order.repositories;

import edu.pjatk.planista.order.models.OrderStatus;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "order-status",
        collectionResourceRel = "order-status"
)
public interface OrderStatusRepository extends DictItemRepository<OrderStatus> {
}
