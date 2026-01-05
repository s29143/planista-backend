package edu.pjatk.planista.company.services;

import edu.pjatk.planista.company.repositories.CompanyRepository;
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
public class CompanyOrderService {
    private final CompanyRepository companyRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Long companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Company " + companyId + " not found");
        }

        return orderRepository
                .findByCompanyId(companyId, pageable)
                .map(mapper::toResponse);
    }
}
