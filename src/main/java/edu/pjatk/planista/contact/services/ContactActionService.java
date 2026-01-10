package edu.pjatk.planista.contact.services;

import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.shared.kernel.ports.ActionQueryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactActionService {
    private final ContactRepository contactRepository;
    private final ActionQueryPort actionQueryPort;

    @Transactional(readOnly = true)
    public Page<ActionResponse> getActions(Long contactId, Pageable pageable) {
        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException("Contact " + contactId + " not found");
        }

        return actionQueryPort.findByContactId(contactId, pageable);
    }
}
