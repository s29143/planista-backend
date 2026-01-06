package edu.pjatk.planista.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.action.controllers.ActionController;
import edu.pjatk.planista.action.dto.ActionRequest;
import edu.pjatk.planista.action.dto.ActionResponse;
import edu.pjatk.planista.action.services.ActionService;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.contact.dto.ContactResponse;
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

@WebMvcTest(ActionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ActionsControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ActionService service;

    private ActionResponse sampleResponse() {
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

        return new ActionResponse(
                1L,
                LocalDate.now(),
                "Some description",
                false,
                true,
                false,
                Instant.now(),
                Instant.now(),
                new DictItemDto(30L, "test"),
                company,
                contact,
                new DictItemDto(10L, "test")
        );
    }

    @Test
    void getReturnsActions() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/actions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.date").value(  LocalDate.now().toString()))
                .andExpect(jsonPath("$.prior").value(true))
                .andExpect(jsonPath("$.user.id").value(30L));
    }

    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any(), any())).thenReturn(page);

        mvc.perform(get("/api/v1/actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].prior").value(true))
                .andExpect(jsonPath("$.content[0].user.id").value(30L));
    }

    @Test
    void createReturnsCreatedAction() throws Exception {
        var request = new ActionRequest(
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                10L,
                20L,
                30L,
                null
        );
        var response = sampleResponse();

        Mockito.when(service.create(any())).thenReturn(response);

        mvc.perform(post("/api/v1/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Some description"));
    }

    @Test
    void updateReturnsUpdatedAction() throws Exception {
        var request = new ActionRequest(
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                10L,
                20L,
                30L,
                null
        );

        var updated = new ActionResponse(
                1L,
                LocalDate.now(),
                "Description",
                true,
                true,
                false,
                Instant.now(),
                Instant.now(),
                null,
                null,
                null,
                null
        );

        Mockito.when(service.update(eq(1L), any())).thenReturn(updated);

        mvc.perform(put("/api/v1/actions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Description"))
                .andExpect(jsonPath("$.prior").value(true));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/actions/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}