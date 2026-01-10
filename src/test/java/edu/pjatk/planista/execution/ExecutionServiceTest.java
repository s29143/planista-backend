package edu.pjatk.planista.execution;

import edu.pjatk.planista.execution.dto.ExecutionRequest;
import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import edu.pjatk.planista.execution.mappers.ExecutionMapper;
import edu.pjatk.planista.execution.models.Execution;
import edu.pjatk.planista.execution.repositories.ExecutionRepository;
import edu.pjatk.planista.execution.services.ExecutionService;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

class ExecutionServiceTest {

    private ExecutionRepository executionRepository;
    private ExecutionMapper mapper;
    private ExecutionService service;

    @BeforeEach
    void setUp() {
        executionRepository = mock(ExecutionRepository.class);
        mapper = mock(ExecutionMapper.class);
        service = new ExecutionService(executionRepository, mapper);
    }

    ProcessResponse processResponse() {
        return new ProcessResponse(
                10L,
                25L,
                1L,
                Instant.now(),
                Instant.now(),
                null,
                null,
                null,
                null
        );
    }

    @Test
    void create_shouldMapAndSave_andReturnResponse() {
        //given
        var req = new ExecutionRequest(
                20L,
                1L,
                1L
        );
        var process = processResponse();
        Execution entityToSave = new Execution();
        Execution saved = new Execution();
        saved.setId(42L);

        ExecutionResponse response = new ExecutionResponse(
                42L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                process
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(executionRepository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        ExecutionResponse result = service.create(req);

        // then
        assertThat(result.id()).isEqualTo(42L);
        then(mapper).should().toEntity(req);
        then(executionRepository).should().save(entityToSave);
        then(mapper).should().toResponse(saved);
    }

    @Test
    void update_shouldUpdateManagedEntity_andReturnResponse() {
        //given
        Long id = 10L;
        var process = processResponse();
        var req = new ExecutionRequest(
                20L,
                1L,
                1L
        );

        Execution existing = new Execution();
        existing.setId(id);

        ExecutionResponse response = new ExecutionResponse(
                10L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                process
        );

        given(executionRepository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        ExecutionResponse result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(executionRepository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        var req = new ExecutionRequest(
                20L,
                1L,
                1L
        );
        given(executionRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Execution " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        Execution entity = new Execution();
        entity.setId(id);
        var process = processResponse();
        ExecutionResponse resp = new ExecutionResponse(
                5L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                process
        );
        given(executionRepository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        ExecutionResponse result = service.get(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void get_shouldThrow_whenNotFound() {
        given(executionRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        Long id = 7L;
        given(executionRepository.existsById(id)).willReturn(true);

        service.delete(id);

        then(executionRepository).should().deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        Long id = 8L;
        given(executionRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void list_shouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Execution e1 = new Execution(); e1.setId(1L);
        Execution e2 = new Execution(); e2.setId(2L);
        Page<Execution> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        var process = processResponse();

        ExecutionResponse r1 = new ExecutionResponse(
                1L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                process
        );
        ExecutionResponse r2 = new ExecutionResponse(2L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                process
                );

        given(executionRepository.findAll(eq(pageable))).willReturn(page);
        given(mapper.toResponse(e1)).willReturn(r1);
        given(mapper.toResponse(e2)).willReturn(r2);

        Page<ExecutionResponse> result = service.list(pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(ExecutionResponse::id)
                .containsExactly(1L, 2L);
    }
}
