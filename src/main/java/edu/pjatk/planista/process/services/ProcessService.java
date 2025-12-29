package edu.pjatk.planista.process.services;

import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.process.dto.ProcessResponse;
import edu.pjatk.planista.process.mappers.ProcessMapper;
import edu.pjatk.planista.process.models.Process;
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
public class ProcessService {

    private final ProcessRepository processRepository;
    private final ProcessMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProcessResponse> list(Pageable pageable) {
        return processRepository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProcessResponse get(Long id) {
        Process entity = processRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Process " + id + " not found"));
        return mapper.toResponse(entity);
    }

    public ProcessResponse create(ProcessRequest req) {
        Process entity = mapper.toEntity(req);
        Process saved = processRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public ProcessResponse update(Long id, ProcessRequest req) {
        Process entity = processRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Process " + id + " not found"));
        mapper.updateEntity(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!processRepository.existsById(id)) {
            throw new EntityNotFoundException("Process " + id + " not found");
        }
        processRepository.deleteById(id);
    }
}