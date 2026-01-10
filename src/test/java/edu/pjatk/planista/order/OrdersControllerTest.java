package edu.pjatk.planista.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.shared.kernel.dto.CompanyResponse;
import edu.pjatk.planista.shared.kernel.dto.ContactResponse;
import edu.pjatk.planista.order.controllers.OrderController;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.order.services.OrderProcessService;
import edu.pjatk.planista.order.services.OrderService;
import edu.pjatk.planista.config.security.JwtAuthenticationFilter;
import edu.pjatk.planista.config.security.JwtService;
import edu.pjatk.planista.shared.dto.DictItemDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrdersControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    OrderService service;

    @MockitoBean
    OrderProcessService processService;

    private OrderResponse sampleResponse() {
        CompanyResponse company = new CompanyResponse(
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

        ContactResponse contact = new ContactResponse(
                1L,
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

        return new OrderResponse(
                1L,
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                company,
                contact,
                new DictItemDto(10L, "test")
        );
    }

    @Test
    void getReturnsOrders() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.product").value("Product"))
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.status.id").value(30L));
    }

    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any(), any())).thenReturn(page);

        mvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].quantity").value(50))
                .andExpect(jsonPath("$.content[0].status.id").value(30L));
    }

    @Test
    void createReturnsCreatedOrder() throws Exception {
        var request = new OrderRequest(
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50,
                10L,
                20L,
                30L,
                null
        );
        var response = sampleResponse();

        Mockito.when(service.create(any())).thenReturn(response);

        mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.product").value("Product"));
    }

    @Test
    void updateReturnsUpdatedOrder() throws Exception {
        var request = new OrderRequest(
                "Product",
                LocalDate.now(),
                LocalDate.now(),
                50,
                10L,
                20L,
                30L,
                null
        );

        var updated = new OrderResponse(
                1L,
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

        Mockito.when(service.update(eq(1L), any())).thenReturn(updated);

        mvc.perform(put("/api/v1/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("Product"))
                .andExpect(jsonPath("$.quantity").value(50));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/orders/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}