package edu.pjatk.planista.contact.services;

import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.shared.kernel.dto.ContactResponse;
import edu.pjatk.planista.shared.kernel.ports.ContactQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ContactQueryService implements ContactQueryPort {
    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Override
    public Page<ContactResponse> findByCompanyId(Long companyId, Pageable pageable) {
        return contactRepository.findByCompanyId(companyId, pageable).map(mapper::toResponse);
    }

    @Override
    public Contact getReferenceById(Long id) {
        return contactRepository.getReferenceById(id);
    }
}