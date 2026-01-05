package edu.pjatk.planista.contact.services;

import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactOrderService {
    private final ContactRepository contactRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Long contactId, Pageable pageable) {
        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException("Contact " + contactId + " not found");
        }

        return orderRepository
                .findByContactId(contactId, pageable)
                .map(mapper::toResponse);
    }
}
