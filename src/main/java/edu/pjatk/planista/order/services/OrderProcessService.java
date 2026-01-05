package edu.pjatk.planista.order.services;

import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.process.dto.ProcessResponse;
import edu.pjatk.planista.process.mappers.ProcessMapper;
import edu.pjatk.planista.process.repositories.ProcessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderProcessService {
    private final OrderRepository orderRepository;
    private final ProcessRepository processRepository;
    private final ProcessMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProcessResponse> getProcesses(Long orderId, Pageable pageable) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order " + orderId + " not found");
        }

        return processRepository
                .findByOrderId(orderId, pageable)
                .map(mapper::toResponse);
    }
}
