package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryPort {
    Page<OrderResponse> findByContactId(Long contactId, Pageable pageable);
    Page<OrderResponse> findByCompanyId(Long companyId, Pageable pageable);
    Order getReferenceById(Long id);
}
