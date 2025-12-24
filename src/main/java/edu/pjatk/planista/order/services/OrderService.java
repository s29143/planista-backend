package edu.pjatk.planista.order.services;

import edu.pjatk.planista.order.dto.OrderFilter;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.order.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.pjatk.planista.order.specs.OrderSpecs.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Transactional(readOnly = true)
    public Page<OrderResponse> list(Pageable pageable, OrderFilter filter) {
        Specification<Order> spec = Specification.allOf(
                searchProductLike(filter.product()),
                statusIdIn(filter.statusIds()),
                searchCompaniesLike(filter.company()),
                searchLike(filter.search())
        );
        return orderRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order " + id + " not found"));
        return mapper.toResponse(entity);
    }

    public OrderResponse create(OrderRequest req) {
        Order entity = mapper.toEntity(req);
        Order saved = orderRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public OrderResponse update(Long id, OrderRequest req) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order " + id + " not found"));
        mapper.updateEntity(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order " + id + " not found");
        }
        orderRepository.deleteById(id);
    }
}