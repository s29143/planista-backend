package edu.pjatk.planista.users;

import edu.pjatk.planista.auth.dto.UserDto;
import edu.pjatk.planista.security.JwtAuthenticationFilter;
import edu.pjatk.planista.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private UserDto sampleResponse() {
        return new UserDto(
                1L,
                "jdoe",
                "John",
                "Doe"
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
}
