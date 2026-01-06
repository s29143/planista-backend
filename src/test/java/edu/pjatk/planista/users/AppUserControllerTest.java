package edu.pjatk.planista.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.auth.dto.UserDto;
import edu.pjatk.planista.config.security.JwtAuthenticationFilter;
import edu.pjatk.planista.config.security.JwtService;
import edu.pjatk.planista.users.dto.UserRequest;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AppUserControllerTest {
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    AppUserService service;

    @Autowired
    ObjectMapper mapper;

    private UserDto sampleResponse() {
        return new UserDto(
                1L,
                "jdoe",
                "John",
                "Doe",
                null
        );
    }
    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any(), any())).thenReturn(page);

        mvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].username").value("jdoe"))
                .andExpect(jsonPath("$.content[0].firstname").value("John"))
                .andExpect(jsonPath("$.content[0].lastname").value("Doe"));
    }

    @Test
    void getReturnsUser() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    @Test
    void createReturnsCreatedUser() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.create(any())).thenReturn(response);
        var request = new UserRequest(
                "John",
                "Doe",
                "jdoe",
                "Ch@ngeM3123!@#",
                null
        );
        mvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));

        Mockito.verify(service).create(any());
    }

    @Test
    void updateReturnsUpdatedUser() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.update(eq(1L), any())).thenReturn(response);
        var request = new UserRequest(
                "John",
                "Doe",
                "jdoe",
                null,
                null
        );
        mvc.perform(
                        put("/api/v1/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));

        Mockito.verify(service).update(eq(1L), any());
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}
