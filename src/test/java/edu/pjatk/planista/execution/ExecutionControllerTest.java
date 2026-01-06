package edu.pjatk.planista.execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.execution.controllers.ExecutionController;
import edu.pjatk.planista.execution.dto.ExecutionRequest;
import edu.pjatk.planista.execution.dto.ExecutionResponse;
import edu.pjatk.planista.execution.services.ExecutionService;
import edu.pjatk.planista.process.dto.ProcessResponse;
import edu.pjatk.planista.config.security.JwtAuthenticationFilter;
import edu.pjatk.planista.config.security.JwtService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExecutionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExecutionControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ExecutionService service;

    private ExecutionResponse sampleResponse() {
        ProcessResponse process = new ProcessResponse(
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

        return new ExecutionResponse(
                1L,
                20L,
                1L,
                Instant.now(),
                Instant.now(),
                process
        );
    }

    @Test
    void getReturnsExecutions() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/executions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.process.id").value(10L));
    }

    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any())).thenReturn(page);

        mvc.perform(get("/api/v1/executions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].quantity").value(20))
                .andExpect(jsonPath("$.content[0].process.id").value(10L));
    }

    @Test
    void createReturnsCreatedExecution() throws Exception {
        var request = new ExecutionRequest(
                20L,
                1L,
                1L
        );
        var response = sampleResponse();

        Mockito.when(service.create(any())).thenReturn(response);

        mvc.perform(post("/api/v1/executions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    void updateReturnsUpdatedExecution() throws Exception {
        var request = new ExecutionRequest(
                20L,
                1L,
                1L
        );

        var updated = sampleResponse();

        Mockito.when(service.update(eq(1L), any())).thenReturn(updated);

        mvc.perform(put("/api/v1/executions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/executions/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}