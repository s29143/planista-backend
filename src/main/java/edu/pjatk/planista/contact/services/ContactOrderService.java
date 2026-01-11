package edu.pjatk.planista.contact.services;

import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.shared.kernel.ports.OrderQueryPort;
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
    private final OrderQueryPort orderQueryPort;

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Long contactId, Pageable pageable) {
        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException("Contact " + contactId + " not found");
        }

        return orderQueryPort.findByContactId(contactId, pageable);
    }
}
