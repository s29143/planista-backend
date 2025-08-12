package edu.pjatk.planista.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.auth.dto.RefreshRequest;
import edu.pjatk.planista.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private AppUserService authService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void loginEndpointReturnsTokens() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("access-xxx", "refresh-yyy"));

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new LoginRequest("admin","admin123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-xxx"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-yyy"));
    }

    @Test
    void refreshEndpointReturnsNewPair() throws Exception {
        when(authService.refresh(any(RefreshRequest.class)))
                .thenReturn(new AuthResponse("access-new", "refresh-new"));

        mvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new RefreshRequest("refresh-old"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-new"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-new"));
    }
}
