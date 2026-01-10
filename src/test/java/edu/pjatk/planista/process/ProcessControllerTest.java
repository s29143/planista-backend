package edu.pjatk.planista.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.process.controllers.ProcessController;
import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import edu.pjatk.planista.process.services.ProcessExecutionService;
import edu.pjatk.planista.process.services.ProcessService;
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

@WebMvcTest(ProcessController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProcessControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ProcessService service;

    @MockitoBean
    ProcessExecutionService executionService;

    private ProcessResponse sampleResponse() {
        OrderResponse order = new OrderResponse(
                10L,"N", LocalDate.now(), LocalDate.now(),
                25L,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null,
                null
        );

        return new ProcessResponse(
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
    }

    @Test
    void getReturnsProcesses() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/processes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.order.id").value(10L));
    }

    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any())).thenReturn(page);

        mvc.perform(get("/api/v1/processes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].quantity").value(20))
                .andExpect(jsonPath("$.content[0].status.id").value(30L));
    }

    @Test
    void createReturnsCreatedProcess() throws Exception {
        var request = new ProcessRequest(
                20L,
                1L,
                1L,
                10L,
                20L,
                30L
        );
        var response = sampleResponse();

        Mockito.when(service.create(any())).thenReturn(response);

        mvc.perform(post("/api/v1/processes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    void updateReturnsUpdatedProcess() throws Exception {
        var request = new ProcessRequest(
                20L,
                1L,
                1L,
                10L,
                20L,
                30L
        );

        var updated = new ProcessResponse(
                1L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                null,
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test"),
                new DictItemDto(30L, "test")
        );

        Mockito.when(service.update(eq(1L), any())).thenReturn(updated);

        mvc.perform(put("/api/v1/processes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/processes/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}