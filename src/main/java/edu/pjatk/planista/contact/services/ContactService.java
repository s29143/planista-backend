package edu.pjatk.planista.contact.services;

import edu.pjatk.planista.contact.dto.ContactFilter;
import edu.pjatk.planista.contact.dto.ContactRequest;
import edu.pjatk.planista.contact.dto.ContactResponse;
import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.contact.repositories.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.pjatk.planista.contact.specs.ContactSpecs.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContactResponse> list(Pageable pageable, ContactFilter filter) {
        Specification<Contact> spec = Specification.allOf(
                userIdIn(filter.userIds()),
                statusIdIn(filter.statusIds()),
                searchCompaniesLike(filter.company()),
                searchLike(filter.search())
        );
        return contactRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContactResponse get(Long id) {
        Contact entity = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact " + id + " not found"));
        return mapper.toResponse(entity);
    }

    public ContactResponse create(ContactRequest req) {
        Contact entity = mapper.toEntity(req);
        Contact saved = contactRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public ContactResponse update(Long id, ContactRequest req) {
        Contact entity = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact " + id + " not found"));
        mapper.updateEntity(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new EntityNotFoundException("Contact " + id + " not found");
        }
        contactRepository.deleteById(id);
    }
}