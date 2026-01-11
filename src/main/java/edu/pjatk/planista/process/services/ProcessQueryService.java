package edu.pjatk.planista.process.services;

import edu.pjatk.planista.process.mappers.ProcessMapper;
import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.process.repositories.ProcessRepository;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import edu.pjatk.planista.shared.kernel.ports.ProcessQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ProcessQueryService implements ProcessQueryPort {
    private final ProcessRepository processRepository;
    private final ProcessMapper mapper;

    @Override
    public Page<ProcessResponse> findByOrderId(Long orderId, Pageable pageable) {
        return processRepository.findByOrderId(orderId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Process getReferenceById(Long id) {
        return processRepository.getReferenceById(id);
    }
}