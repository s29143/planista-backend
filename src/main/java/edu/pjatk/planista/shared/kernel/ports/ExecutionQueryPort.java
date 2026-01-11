package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExecutionQueryPort {
    Page<ExecutionResponse> findByProcessId(Long processId, Pageable pageable);
}
