package edu.pjatk.planista.order;

import edu.pjatk.planista.order.dto.OrderFilter;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.order.services.OrderService;
import edu.pjatk.planista.shared.dto.DictItemDto;
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

class OrdersServiceTest {

    private OrderRepository repository;
    private OrderMapper mapper;
    private OrderService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderRepository.class);
        mapper = mock(OrderMapper.class);
        service = new OrderService(repository, mapper);
    }

    @Test
    void create_shouldMapAndSave_andReturnResponse() {
        //given
        var req = new OrderRequest(
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50,
                10L,
                20L,
                30L,
                null
        );
        Order entityToSave = new Order();
        Order saved = new Order();
        saved.setId(42L);

        OrderResponse response = new OrderResponse(
                42L,
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                null,
                null,
                new DictItemDto(10L, "test")
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(repository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        OrderResponse result = service.create(req);

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
        var req = new OrderRequest(
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50,
                10L,
                20L,
                30L,
                null
        );

        Order existing = new Order();
        existing.setId(id);

        OrderResponse response = new OrderResponse(
                id,
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                null,
                null,
                new DictItemDto(10L, "test")
        );

        given(repository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        OrderResponse result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(repository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        var req = new OrderRequest(
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50,
                10L,
                20L,
                30L,
                null
        );
        given(repository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        Order entity = new Order();
        entity.setId(id);
        OrderResponse resp = new OrderResponse(
                id,
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                null,
                null,
                new DictItemDto(10L, "test")
        );
        given(repository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        OrderResponse result = service.get(id);
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
        OrderFilter filter = new OrderFilter(null, null, null, null);
        Order e1 = new Order(); e1.setId(1L);
        Order e2 = new Order(); e2.setId(2L);
        Page<Order> page = new PageImpl<>(List.of(e1, e2), pageable, 2);

        OrderResponse r1 = new OrderResponse(1L,
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                null,
                null,
                new DictItemDto(10L, "test")
        );
        OrderResponse r2 = new OrderResponse(2L,
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                null,
                null,
                new DictItemDto(10L, "test")
                );

        given(repository.findAll(any(Specification.class), eq(pageable))).willReturn(page);
        given(mapper.toResponse(e1)).willReturn(r1);
        given(mapper.toResponse(e2)).willReturn(r2);

        Page<OrderResponse> result = service.list(pageable, filter);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(OrderResponse::id)
                .containsExactly(1L, 2L);
    }
}
