package edu.pjatk.planista.contact.services;

import edu.pjatk.planista.action.dto.ActionResponse;
import edu.pjatk.planista.action.mappers.ActionMapper;
import edu.pjatk.planista.action.repositories.ActionRepository;
import edu.pjatk.planista.contact.repositories.ContactRepository;
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
    private final ActionRepository actionRepository;
    private final ActionMapper mapper;

    @Transactional(readOnly = true)
    public Page<ActionResponse> getActions(Long contactId, Pageable pageable) {
        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException("Contact " + contactId + " not found");
        }

        return actionRepository
                .findByContactId(contactId, pageable)
                .map(mapper::toResponse);
    }
}
