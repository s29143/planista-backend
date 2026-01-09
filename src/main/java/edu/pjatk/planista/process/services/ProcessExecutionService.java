package edu.pjatk.planista.process.services;

import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import edu.pjatk.planista.execution.mappers.ExecutionMapper;
import edu.pjatk.planista.execution.repositories.ExecutionRepository;
import edu.pjatk.planista.process.repositories.ProcessRepository;
import edu.pjatk.planista.shared.kernel.ports.ExecutionQueryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcessExecutionService {
    private final ProcessRepository processRepository;
    private final ExecutionQueryPort executionQueryPort;

    @Transactional(readOnly = true)
    public Page<ExecutionResponse> getProcesses(Long processId, Pageable pageable) {
        if (!processRepository.existsById(processId)) {
            throw new EntityNotFoundException("Process " + processId + " not found");
        }

        return executionQueryPort.findByProcessId(processId, pageable);
    }
}
