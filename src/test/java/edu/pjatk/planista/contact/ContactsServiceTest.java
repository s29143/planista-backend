package edu.pjatk.planista.contact;

import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.contact.dto.ContactFilter;
import edu.pjatk.planista.contact.dto.ContactRequest;
import edu.pjatk.planista.contact.dto.ContactResponse;
import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.contact.services.ContactService;
import edu.pjatk.planista.shared.dto.DictItemDto;
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

class ContactsServiceTest {

    private ContactRepository ContactRepository;
    private ContactMapper mapper;
    private ContactService service;

    @BeforeEach
    void setUp() {
        ContactRepository = mock(ContactRepository.class);
        mapper = mock(ContactMapper.class);
        service = new ContactService(ContactRepository, mapper);
    }

    CompanyResponse companyResponse() {
        return new CompanyResponse(
                10L,"N","F","1234567890",
                null,null,null,null,null,null,null,null,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null,
                null
        );
    }

    @Test
    void create_shouldMapAndSave_andReturnResponse() {
        //given
        var req = new ContactRequest(
                "John",
                "Doe",
                "Salary man",
                "22 23 23 13",
                "123 123 123",
                "info@abc.pl",
                true,
                true,
                10L,
                20L,
                30L
        );
        var company = companyResponse();
        Contact entityToSave = new Contact();
        Contact saved = new Contact();
        saved.setId(42L);

        ContactResponse response = new ContactResponse(
                42L,
                "John",
                "Doe",
                "Salary man",
                "22 23 23 13",
                "123 123 123",
                "jdoe@example.com",
                true,
                true,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                company,
                new DictItemDto(30L, "test")
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(ContactRepository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        ContactResponse result = service.create(req);

        // then
        assertThat(result.id()).isEqualTo(42L);
        then(mapper).should().toEntity(req);
        then(ContactRepository).should().save(entityToSave);
        then(mapper).should().toResponse(saved);
    }

    @Test
    void update_shouldUpdateManagedEntity_andReturnResponse() {
        //given
        Long id = 10L;
        var company = companyResponse();
        var req = new ContactRequest(
                "John",
                "Doe",
                "Salary man",
                "22 23 23 13",
                "123 123 123",
                "info@abc.pl",
                true,
                true,
                10L,
                20L,
                30L
        );

        Contact existing = new Contact();
        existing.setId(id);

        ContactResponse response = new ContactResponse(
                id,
                "John",
                "Doe",
                "Salary man",
                "22 23 23 13",
                "123 123 123",
                "jdoe@example.com",
                true,
                true,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                company,
                new DictItemDto(30L, "test")
        );

        given(ContactRepository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        ContactResponse result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(ContactRepository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        var req = new ContactRequest(
                "John",
                "Doe",
                "Salary man",
                "22 23 23 13",
                "123 123 123",
                "info@abc.pl",
                true,
                true,
                10L,
                20L,
                30L
        );
        given(ContactRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Contact " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        Contact entity = new Contact();
        entity.setId(id);
        var company = companyResponse();
        ContactResponse resp = new ContactResponse(
                id,
                "John",
                "Doe",
                "Salary man",
                "22 23 23 13",
                "123 123 123",
                "jdoe@example.com",
                true,
                true,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                company,
                new DictItemDto(30L, "test")
        );
        given(ContactRepository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        ContactResponse result = service.get(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void get_shouldThrow_whenNotFound() {
        given(ContactRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        Long id = 7L;
        given(ContactRepository.existsById(id)).willReturn(true);

        service.delete(id);

        then(ContactRepository).should().deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        Long id = 8L;
        given(ContactRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void list_shouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        ContactFilter ContactFilter = new ContactFilter(null, null, null, null);
        Contact e1 = new Contact(); e1.setId(1L);
        Contact e2 = new Contact(); e2.setId(2L);
        Page<Contact> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        var company = companyResponse();

        ContactResponse r1 = new ContactResponse(1L,null,null,null,null,null,null,false, false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                company,
                new DictItemDto(1L, "test")
        );
        ContactResponse r2 = new ContactResponse(2L,null,null,null,null,null,null,false, false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                company,
                new DictItemDto(1L, "test")
                );

        given(ContactRepository.findAll(any(Specification.class), eq(pageable))).willReturn(page);
        given(mapper.toResponse(e1)).willReturn(r1);
        given(mapper.toResponse(e2)).willReturn(r2);

        Page<ContactResponse> result = service.list(pageable, ContactFilter);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(ContactResponse::id)
                .containsExactly(1L, 2L);
    }
}
