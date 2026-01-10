package edu.pjatk.planista.process.services;

import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.process.mappers.ProcessMapper;
import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.process.repositories.ProcessRepository;
import edu.pjatk.planista.process.repositories.ProcessStatusRepository;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import edu.pjatk.planista.shared.kernel.ports.OrderQueryPort;
import edu.pjatk.planista.shared.repositories.TechnologyRepository;
import edu.pjatk.planista.shared.repositories.WorkstationRepository;
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

    private final ProcessStatusRepository statusRepository;
    private final WorkstationRepository workstationRepository;
    private final TechnologyRepository technologyRepository;
    private final OrderQueryPort orderQueryPort;

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
        applyRelations(entity, req);
        Process saved = processRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public ProcessResponse update(Long id, ProcessRequest req) {
        Process entity = processRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Process " + id + " not found"));
        mapper.updateEntity(entity, req);
        applyRelations(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!processRepository.existsById(id)) {
            throw new EntityNotFoundException("Process " + id + " not found");
        }
        processRepository.deleteById(id);
    }

    private void applyRelations(Process entity, ProcessRequest req) {
        if(req.orderId() == null) {
            throw new IllegalArgumentException("orderId must not be null");
        }
        entity.setStatus(req.statusId() != null
                ? statusRepository.getReferenceById(req.statusId())
                : null);

        entity.setWorkstation(req.workstationId() != null
                ? workstationRepository.getReferenceById(req.workstationId())
                : null);

        entity.setTechnology(req.technologyId() != null
                ? technologyRepository.getReferenceById(req.technologyId())
                : null);

        entity.setOrder(orderQueryPort.getReferenceById(req.orderId()));
    }
}