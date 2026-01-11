package edu.pjatk.planista.process;

import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.process.mappers.ProcessMapper;
import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.process.repositories.ProcessRepository;
import edu.pjatk.planista.process.repositories.ProcessStatusRepository;
import edu.pjatk.planista.process.services.ProcessService;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import edu.pjatk.planista.shared.kernel.ports.OrderQueryPort;
import edu.pjatk.planista.shared.repositories.TechnologyRepository;
import edu.pjatk.planista.shared.repositories.WorkstationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

class ProcessServiceTest {

    private ProcessRepository processRepository;
    private ProcessMapper mapper;
    private ProcessService service;
    private ProcessStatusRepository statusRepository;
    private WorkstationRepository workstationRepository;
    private TechnologyRepository technologyRepository;
    private OrderQueryPort orderQueryPort;

    @BeforeEach
    void setUp() {
        processRepository = mock(ProcessRepository.class);
        mapper = mock(ProcessMapper.class);
        statusRepository = mock(ProcessStatusRepository.class);
        workstationRepository = mock(WorkstationRepository.class);
        technologyRepository = mock(TechnologyRepository.class);
        orderQueryPort = mock(OrderQueryPort.class);
        service = new ProcessService(processRepository, mapper,  statusRepository, workstationRepository, technologyRepository, orderQueryPort);
    }

    OrderResponse orderResponse() {
        return new OrderResponse(
                10L,"N", LocalDate.now(), LocalDate.now(),
                25L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null
        );
    }

    @Test
    void create_shouldMapAndSave_andReturnResponse() {
        //given
        var req = new ProcessRequest(
                20L,
                1L,
                1L,
                10L,
                20L,
                30L
        );
        var order = orderResponse();
        Process entityToSave = new Process();
        Process saved = new Process();
        saved.setId(42L);

        ProcessResponse response = new ProcessResponse(
                42L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test"),
                new DictItemDto(30L, "test")
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(processRepository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        ProcessResponse result = service.create(req);

        // then
        assertThat(result.id()).isEqualTo(42L);
        then(mapper).should().toEntity(req);
        then(processRepository).should().save(entityToSave);
        then(mapper).should().toResponse(saved);
    }

    @Test
    void update_shouldUpdateManagedEntity_andReturnResponse() {
        //given
        Long id = 10L;
        var order = orderResponse();
        var req = new ProcessRequest(
                20L,
                1L,
                1L,
                10L,
                20L,
                30L
        );

        Process existing = new Process();
        existing.setId(id);

        ProcessResponse response = new ProcessResponse(
                10L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test"),
                new DictItemDto(30L, "test")
        );

        given(processRepository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        ProcessResponse result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(processRepository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        var req = new ProcessRequest(
                20L,
                1L,
                1L,
                10L,
                20L,
                30L
        );
        given(processRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Process " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        Process entity = new Process();
        entity.setId(id);
        var order = orderResponse();
        ProcessResponse resp = new ProcessResponse(
                5L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test"),
                new DictItemDto(30L, "test")
        );
        given(processRepository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        ProcessResponse result = service.get(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void get_shouldThrow_whenNotFound() {
        given(processRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        Long id = 7L;
        given(processRepository.existsById(id)).willReturn(true);

        service.delete(id);

        then(processRepository).should().deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        Long id = 8L;
        given(processRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void list_shouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Process e1 = new Process(); e1.setId(1L);
        Process e2 = new Process(); e2.setId(2L);
        Page<Process> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        var order = orderResponse();

        ProcessResponse r1 = new ProcessResponse(
                1L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test"),
                new DictItemDto(30L, "test")
        );
        ProcessResponse r2 = new ProcessResponse(2L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(10L, "test"),
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test"),
                new DictItemDto(30L, "test")
                );

        given(processRepository.findAll(eq(pageable))).willReturn(page);
        given(mapper.toResponse(e1)).willReturn(r1);
        given(mapper.toResponse(e2)).willReturn(r2);

        Page<ProcessResponse> result = service.list(pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(ProcessResponse::id)
                .containsExactly(1L, 2L);
    }
}
