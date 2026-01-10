package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActionQueryPort {
    Page<ActionResponse> findByCompanyId(Long companyId, Pageable pageable);
    Page<ActionResponse> findByContactId(Long contactId, Pageable pageable);
}
