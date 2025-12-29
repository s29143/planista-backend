package edu.pjatk.planista.execution.services;

import edu.pjatk.planista.execution.dto.ExecutionRequest;
import edu.pjatk.planista.execution.dto.ExecutionResponse;
import edu.pjatk.planista.execution.mappers.ExecutionMapper;
import edu.pjatk.planista.execution.models.Execution;
import edu.pjatk.planista.execution.repositories.ExecutionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ExecutionMapper mapper;

    @Transactional(readOnly = true)
    public Page<ExecutionResponse> list(Pageable pageable) {
        return executionRepository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ExecutionResponse get(Long id) {
        Execution entity = executionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Execution " + id + " not found"));
        return mapper.toResponse(entity);
    }

    public ExecutionResponse create(ExecutionRequest req) {
        Execution entity = mapper.toEntity(req);
        Execution saved = executionRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public ExecutionResponse update(Long id, ExecutionRequest req) {
        Execution entity = executionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Execution " + id + " not found"));
        mapper.updateEntity(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!executionRepository.existsById(id)) {
            throw new EntityNotFoundException("Execution " + id + " not found");
        }
        executionRepository.deleteById(id);
    }
}