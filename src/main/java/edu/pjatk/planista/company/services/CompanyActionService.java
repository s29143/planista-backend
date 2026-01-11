package edu.pjatk.planista.company.services;

import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.shared.kernel.ports.ActionQueryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyActionService {
    private final CompanyRepository companyRepository;
    private final ActionQueryPort actionQueryPort;

    @Transactional(readOnly = true)
    public Page<ActionResponse> getActions(Long companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Company " + companyId + " not found");
        }

        return actionQueryPort.findByCompanyId(companyId, pageable);
    }
}
