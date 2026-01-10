package edu.pjatk.planista.action.services;

import edu.pjatk.planista.action.mappers.ActionMapper;
import edu.pjatk.planista.action.repositories.ActionRepository;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.shared.kernel.ports.ActionQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ActionQueryService implements ActionQueryPort {
    private final ActionRepository actionRepository;
    private final ActionMapper mapper;

    @Override
    public Page<ActionResponse> findByCompanyId(Long companyId, Pageable pageable) {
        return actionRepository.findByCompanyId(companyId, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<ActionResponse> findByContactId(Long contactId, Pageable pageable) {
        return actionRepository.findByContactId(contactId, pageable).map(mapper::toResponse);
    }
}