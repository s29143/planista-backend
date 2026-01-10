package edu.pjatk.planista.company;

import edu.pjatk.planista.company.dto.CompanyFilter;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.company.repositories.CompanyAcquiredRepository;
import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.company.repositories.CompanyStatusRepository;
import edu.pjatk.planista.company.services.CompanyService;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.kernel.dto.CompanyResponse;
import edu.pjatk.planista.shared.kernel.ports.UserQueryPort;
import edu.pjatk.planista.shared.repositories.CountryRepository;
import edu.pjatk.planista.shared.repositories.DistrictRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

class CompanyServiceTest {

    private CompanyRepository companyRepository;
    private CompanyMapper mapper;
    private UserQueryPort userQueryPort;
    private CompanyAcquiredRepository acquiredRepository;
    private DistrictRepository districtRepository;
    private CountryRepository countryRepository;
    private CompanyStatusRepository statusRepository;
    private CompanyService service;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        mapper = mock(CompanyMapper.class);
        userQueryPort = mock(UserQueryPort.class);
        acquiredRepository = mock(CompanyAcquiredRepository.class);
        districtRepository = mock(DistrictRepository.class);
        countryRepository = mock(CountryRepository.class);
        statusRepository = mock(CompanyStatusRepository.class);
        service = new CompanyService(companyRepository, mapper, userQueryPort, acquiredRepository, districtRepository, countryRepository, statusRepository);
    }

    @Test
    void create_shouldMapAndSave_andReturnResponse() {
        //given
        CompanyRequest req = new CompanyRequest("ACME","ACME Sp. z o.o.","1234567890",
                "00-001","Street","1","2","123456789","a@b.com","https://acme.com","notes",
                1L, 2L, 3L, 4L, 5L);

        Company entityToSave = new Company();
        Company saved = new Company();
        saved.setId(42L);

        CompanyResponse response = new CompanyResponse(
                42L,"ACME","ACME Sp. z o.o.","1234567890",
                "00-001","Street","1","2","123456789","a@b.com","https://acme.com","notes",
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null,
                null
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(companyRepository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        CompanyResponse result = service.create(req);

        // then
        assertThat(result.id()).isEqualTo(42L);
        then(mapper).should().toEntity(req);
        then(companyRepository).should().save(entityToSave);
        then(mapper).should().toResponse(saved);
    }

    @Test
    void update_shouldUpdateManagedEntity_andReturnResponse() {
        //given
        Long id = 10L;
        CompanyRequest req = new CompanyRequest("N","F","1234567890",
                null,null,null,null,null,null,null,null,
                1L, null, null, null, null);

        Company existing = new Company();
        existing.setId(id);

        CompanyResponse response = new CompanyResponse(
                id,"N","F","1234567890",
                null,null,null,null,null,null,null,null,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null,
                null
        );

        given(companyRepository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        CompanyResponse result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(companyRepository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        CompanyRequest req = new CompanyRequest("N","F","1234567890",
                null,null,null,null,null,null,null,null,
                1L, null,null,null,null
        );
        given(companyRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Company " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        Company entity = new Company();
        entity.setId(id);
        CompanyResponse resp = new CompanyResponse(id,"s","f","1234567890",
                null,null,null,null,null,null,null,null,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null,
                null
                );

        given(companyRepository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        CompanyResponse result = service.get(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void get_shouldThrow_whenNotFound() {
        given(companyRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        Long id = 7L;
        given(companyRepository.existsById(id)).willReturn(true);

        service.delete(id);

        then(companyRepository).should().deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        Long id = 8L;
        given(companyRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void list_shouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        CompanyFilter companyFilter = new CompanyFilter(null, null, null, null);
        Company e1 = new Company(); e1.setId(1L);
        Company e2 = new Company(); e2.setId(2L);
        Page<Company> page = new PageImpl<>(List.of(e1, e2), pageable, 2);

        CompanyResponse r1 = new CompanyResponse(1L,null,null,null,null,null,null,null,null,
                null,null,null,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null,
                null);
        CompanyResponse r2 = new CompanyResponse(2L,null,null,null,null,null,null,null,null,
                null,null,null,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null,
                null);

        given(companyRepository.findAll(any(Specification.class), eq(pageable))).willReturn(page);
        given(mapper.toResponse(e1)).willReturn(r1);
        given(mapper.toResponse(e2)).willReturn(r2);

        Page<CompanyResponse> result = service.list(pageable, companyFilter);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(CompanyResponse::id)
                .containsExactly(1L, 2L);
    }
}
