package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.shared.kernel.dto.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactQueryPort {
    Page<ContactResponse> findByCompanyId(Long companyId, Pageable pageable);
    Contact getReferenceById(Long id);
}
