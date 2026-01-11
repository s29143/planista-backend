package edu.pjatk.planista.company.services;

import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.shared.kernel.ports.OrderQueryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyOrderService {
    private final CompanyRepository companyRepository;
    private final OrderQueryPort orderQueryPort;

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Long companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Company " + companyId + " not found");
        }

        return orderQueryPort.findByCompanyId(companyId, pageable);
    }
}
