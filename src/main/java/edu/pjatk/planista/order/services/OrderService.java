package edu.pjatk.planista.order.services;

import edu.pjatk.planista.order.dto.OrderFilter;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.order.repositories.OrderStatusRepository;
import edu.pjatk.planista.order.repositories.OrderTypeRepository;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.shared.kernel.ports.CompanyQueryPort;
import edu.pjatk.planista.shared.kernel.ports.ContactQueryPort;
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

    private final OrderStatusRepository statusRepository;
    private final OrderTypeRepository typeRepository;
    private final CompanyQueryPort companyQueryPort;
    private final ContactQueryPort contactQueryPort;

    @Transactional(readOnly = true)
    public Page<OrderResponse> list(Pageable pageable, OrderFilter filter) {
        Specification<Order> spec = Specification.allOf(
                searchProductLike(filter.product()),
                statusIdIn(filter.statusIds()),
                searchCompaniesLike(filter.company()),
                typeIdIn(filter.typeIds())
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
        applyRelations(entity, req);
        Order saved = orderRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public OrderResponse update(Long id, OrderRequest req) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order " + id + " not found"));
        mapper.updateEntity(entity, req);
        applyRelations(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    private void applyRelations(Order entity, OrderRequest req) {
        entity.setStatus(req.statusId() != null
                ? statusRepository.getReferenceById(req.statusId())
                : null);

        entity.setType(req.typeId() != null
                ? typeRepository.getReferenceById(req.typeId())
                : null);

        entity.setCompany(req.companyId() != null
                ? companyQueryPort.getReferenceById(req.companyId())
                : null);

        entity.setContact(req.contactId() != null
                ? contactQueryPort.getReferenceById(req.contactId())
                : null);
    }
}