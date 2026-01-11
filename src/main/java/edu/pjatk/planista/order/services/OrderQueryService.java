package edu.pjatk.planista.order.services;

import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.shared.kernel.ports.OrderQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class OrderQueryService implements OrderQueryPort {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Override
    public Page<OrderResponse> findByContactId(Long contactId, Pageable pageable) {
        return orderRepository.findByContactId(contactId, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<OrderResponse> findByCompanyId(Long companyId, Pageable pageable) {
        return orderRepository.findByCompanyId(companyId, pageable).map(mapper::toResponse);
    }

    @Override
    public Order getReferenceById(Long id) {
        return orderRepository.getReferenceById(id);
    }
}