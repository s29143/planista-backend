package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProcessQueryPort {
    Page<ProcessResponse> findByOrderId(Long orderId, Pageable pageable);
    Process getReferenceById(Long id);
}
