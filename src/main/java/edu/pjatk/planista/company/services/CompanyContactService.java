package edu.pjatk.planista.company.services;

import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.contact.dto.ContactResponse;
import edu.pjatk.planista.contact.mappers.ContactMapper;
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
public class CompanyContactService {
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContactResponse> getContacts(Long companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Company " + companyId + " not found");
        }

        return contactRepository
                .findByCompanyId(companyId, pageable)
                .map(mapper::toResponse);
    }
}
