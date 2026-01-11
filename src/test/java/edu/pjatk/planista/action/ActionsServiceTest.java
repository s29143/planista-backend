package edu.pjatk.planista.action;

import edu.pjatk.planista.action.dto.ActionFilter;
import edu.pjatk.planista.action.dto.ActionRequest;
import edu.pjatk.planista.action.mappers.ActionMapper;
import edu.pjatk.planista.action.models.Action;
import edu.pjatk.planista.action.repositories.ActionRepository;
import edu.pjatk.planista.action.repositories.ActionTypeRepository;
import edu.pjatk.planista.action.services.ActionService;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.shared.kernel.ports.CompanyQueryPort;
import edu.pjatk.planista.shared.kernel.ports.ContactQueryPort;
import edu.pjatk.planista.shared.kernel.ports.UserQueryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

class ActionsServiceTest {

    private ActionRepository repository;
    private ActionMapper mapper;
    private ActionService service;
    private ActionTypeRepository typeRepository;
    private UserQueryPort userQueryPort;
    private CompanyQueryPort companyQueryPort;
    private ContactQueryPort contactQueryPort;

    @BeforeEach
    void setUp() {
        repository = mock(ActionRepository.class);
        mapper = mock(ActionMapper.class);
        typeRepository = mock(ActionTypeRepository.class);
        userQueryPort = mock(UserQueryPort.class);
        companyQueryPort = mock(CompanyQueryPort.class);
        contactQueryPort = mock(ContactQueryPort.class);
        service = new ActionService(repository, mapper,  typeRepository, userQueryPort, companyQueryPort, contactQueryPort);
    }

    @Test
    void create_shouldMapAndSave_andReturnResponse() {
        //given
        var req = new ActionRequest(
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                1L,
                null,
                null,
                null
        );
        Action entityToSave = new Action();
        Action saved = new Action();
        saved.setId(42L);

        ActionResponse response = new ActionResponse(
                42L,
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                null,
                null,
                null
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(repository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        ActionResponse result = service.create(req);

        // then
        assertThat(result.id()).isEqualTo(42L);
        then(mapper).should().toEntity(req);
        then(repository).should().save(entityToSave);
        then(mapper).should().toResponse(saved);
    }

    @Test
    void update_shouldUpdateManagedEntity_andReturnResponse() {
        //given
        Long id = 10L;
        var req = new ActionRequest(
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                1L,
                null,
                null,
                null
        );

        Action existing = new Action();
        existing.setId(id);

        ActionResponse response = new ActionResponse(
                id,
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                null,
                null,
                null
        );

        given(repository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        ActionResponse result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(repository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        var req = new ActionRequest(
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                1L,
                null,
                null,
                null
        );
        given(repository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Action " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        Action entity = new Action();
        entity.setId(id);
        ActionResponse resp = new ActionResponse(
                id,
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                null,
                null,
                null
        );
        given(repository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        ActionResponse result = service.get(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void get_shouldThrow_whenNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        Long id = 7L;
        given(repository.existsById(id)).willReturn(true);

        service.delete(id);

        then(repository).should().deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        Long id = 8L;
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void list_shouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        ActionFilter filter = new ActionFilter(null, null, null, null);
        Action e1 = new Action(); e1.setId(1L);
        Action e2 = new Action(); e2.setId(2L);
        Page<Action> page = new PageImpl<>(List.of(e1, e2), pageable, 2);

        ActionResponse r1 = new ActionResponse(1L,
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                null,
                null,
                null
        );
        ActionResponse r2 = new ActionResponse(2L,
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                null,
                null,
                null
                );

        given(repository.findAll(any(Specification.class), eq(pageable))).willReturn(page);
        given(mapper.toResponse(e1)).willReturn(r1);
        given(mapper.toResponse(e2)).willReturn(r2);

        Page<ActionResponse> result = service.list(pageable, filter);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(ActionResponse::id)
                .containsExactly(1L, 2L);
    }
}
