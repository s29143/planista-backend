package edu.pjatk.planista.company.services;

import edu.pjatk.planista.action.dto.ActionResponse;
import edu.pjatk.planista.action.mappers.ActionMapper;
import edu.pjatk.planista.action.repositories.ActionRepository;
import edu.pjatk.planista.company.repositories.CompanyRepository;
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
    private final ActionRepository actionRepository;
    private final ActionMapper mapper;

    @Transactional(readOnly = true)
    public Page<ActionResponse> getActions(Long companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Company " + companyId + " not found");
        }

        return actionRepository
                .findByCompanyId(companyId, pageable)
                .map(mapper::toResponse);
    }
}
