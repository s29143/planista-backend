package edu.pjatk.planista.contact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.config.security.JwtAuthenticationFilter;
import edu.pjatk.planista.config.security.JwtService;
import edu.pjatk.planista.contact.controllers.ContactController;
import edu.pjatk.planista.contact.dto.ContactRequest;
import edu.pjatk.planista.contact.services.ContactActionService;
import edu.pjatk.planista.contact.services.ContactOrderService;
import edu.pjatk.planista.contact.services.ContactService;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.kernel.dto.ContactResponse;
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

@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactsControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ContactService service;

    @MockitoBean
    ContactActionService actionService;

    @MockitoBean
    ContactOrderService orderService;

    private ContactResponse sampleResponse() {
        return new ContactResponse(
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
                new DictItemDto(10L, "test"),
                new DictItemDto(30L, "test")
        );
    }

    @Test
    void getReturnsContacts() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/contacts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.user.id").value(10L));
    }

    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any(), any())).thenReturn(page);

        mvc.perform(get("/api/v1/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].status.id").value(30L));
    }

    @Test
    void createReturnsCreatedContact() throws Exception {
        var request = new ContactRequest(
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
        var response = sampleResponse();

        Mockito.when(service.create(any())).thenReturn(response);

        mvc.perform(post("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    void updateReturnsUpdatedContact() throws Exception {
        var request = new ContactRequest(
                "John",
                "Doe",
                "Clerk",
                "23 12 14 13",
                "123 123 123",
                "jdoe@example.com",
                true,
                true,
                11L,
                21L,
                31L
        );

        var updated = new ContactResponse(
                1L,
                "John",
                "Doe",
                "Clerk",
                "23 12 14 13",
                "123 123 123",
                "jdoe@example.com",
                true,
                true,
                Instant.now(),
                Instant.now(),
                new DictItemDto(1L, "test"),
                null,
                null
        );

        Mockito.when(service.update(eq(1L), any())).thenReturn(updated);

        mvc.perform(put("/api/v1/contacts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/contacts/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}