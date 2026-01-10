package edu.pjatk.planista.company.services;

import edu.pjatk.planista.company.dto.CompanyFilter;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.company.repositories.CompanyAcquiredRepository;
import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.company.repositories.CompanyStatusRepository;
import edu.pjatk.planista.shared.kernel.dto.CompanyResponse;
import edu.pjatk.planista.shared.kernel.ports.CompanyQueryPort;
import edu.pjatk.planista.shared.kernel.ports.UserQueryPort;
import edu.pjatk.planista.shared.repositories.CountryRepository;
import edu.pjatk.planista.shared.repositories.DistrictRepository;
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
public class CompanyService implements CompanyQueryPort {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;
    private final UserQueryPort userQueryPort;
    private final CompanyAcquiredRepository acquiredRepository;
    private final DistrictRepository districtRepository;
    private final CountryRepository countryRepository;
    private final CompanyStatusRepository statusRepository;

    @Transactional(readOnly = true)
    public Page<CompanyResponse> list(Pageable pageable, CompanyFilter filter) {
        Specification<Company> spec = Specification.allOf(
                userIdIn(filter.userIds()),
                statusIdIn(filter.statusIds()),
                districtsIdIn(filter.districtsId()),
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
        applyRelations(entity, req);
        Company saved = companyRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public CompanyResponse update(Long id, CompanyRequest req) {
        Company entity = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company " + id + " not found"));
        mapper.updateEntity(entity, req);
        applyRelations(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company " + id + " not found");
        }
        companyRepository.deleteById(id);
    }

    @Override
    public Company getReferenceById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company " + id + " not found"));
    }


    private void applyRelations(Company entity, CompanyRequest req) {
        if (req.userId() != null) {
            entity.setUser(userQueryPort.getReferenceById(req.userId()));
        } else {
            throw new IllegalArgumentException("userId is required");
        }

        entity.setAcquired(req.acquiredId() != null
                ? acquiredRepository.getReferenceById(req.acquiredId())
                : null);

        entity.setDistrict(req.districtId() != null
                ? districtRepository.getReferenceById(req.districtId())
                : null);

        entity.setCountry(req.countryId() != null
                ? countryRepository.getReferenceById(req.countryId())
                : null);

        entity.setStatus(req.statusId() != null
                ? statusRepository.getReferenceById(req.statusId())
                : null);
    }
}