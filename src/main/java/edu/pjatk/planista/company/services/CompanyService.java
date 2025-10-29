package edu.pjatk.planista.company.services;

import edu.pjatk.planista.company.dto.CompanyFilter;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.company.repositories.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.pjatk.planista.company.specs.CompanySpecs.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;

    @Transactional(readOnly = true)
    public Page<CompanyResponse> list(Pageable pageable, CompanyFilter filter) {
        Specification<Company> spec = Specification.allOf(
                userIdIn(filter.userIds()),
                statusIdIn(filter.statusIds()),
                searchLike(filter.search())
        );
        return companyRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CompanyResponse get(Long id) {
        Company entity = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company " + id + " not found"));
        return mapper.toResponse(entity);
    }

    public CompanyResponse create(CompanyRequest req) {
        Company entity = mapper.toEntity(req);
        Company saved = companyRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public CompanyResponse update(Long id, CompanyRequest req) {
        Company entity = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company " + id + " not found"));
        mapper.updateEntity(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company " + id + " not found");
        }
        companyRepository.deleteById(id);
    }
}