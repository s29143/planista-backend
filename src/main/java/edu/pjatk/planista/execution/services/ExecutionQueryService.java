package edu.pjatk.planista.execution.services;

import edu.pjatk.planista.execution.mappers.ExecutionMapper;
import edu.pjatk.planista.execution.repositories.ExecutionRepository;
import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import edu.pjatk.planista.shared.kernel.ports.ExecutionQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ExecutionQueryAdapter implements ExecutionQueryPort {
    private final ExecutionRepository executionRepository;
    private final ExecutionMapper mapper;

    @Override
    public Page<ExecutionResponse> findByProcessId(Long processId, Pageable pageable) {
        return executionRepository.findByProcessId(processId, pageable)
                .map(mapper::toResponse);
    }
}