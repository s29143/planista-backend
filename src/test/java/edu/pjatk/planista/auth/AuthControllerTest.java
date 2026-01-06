package edu.pjatk.planista.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.auth.dto.AuthResponse;
import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.auth.dto.RefreshRequest;
import edu.pjatk.planista.shared.dto.UserDto;
import edu.pjatk.planista.config.security.JwtAuthenticationFilter;
import edu.pjatk.planista.config.security.JwtService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @MockitoBean JwtService jwtService;
    @MockitoBean JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private AuthUserService authService;
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
        when(authService.refresh(any(String.class)))
                .thenReturn(new AuthResponse("access-new", "refresh-new"));

        mvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new RefreshRequest("refresh-old"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-new"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-new"));
    }

    @Test
    @WithMockUser(username = "john", roles = "USER")
    void meReturnsDtoFromService() throws Exception {
        when(authService.me("john")).thenReturn(
                new UserDto(7L, "john", "John", "Doe", null)
        );

        mvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    @Test
    void meWithoutAuthReturns401() throws Exception {
        mvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginSetsHttpOnlyRefreshCookie() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("access-xxx", "refresh-yyy"));

        mvc.perform(post("/api/v1/auth/login")
                        .header("X-Client", "web")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new LoginRequest("admin","admin123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-xxx"))
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().secure("refreshToken", true));
    }

    @Test
    void logoutRemovesRefreshToken() throws Exception {
        mvc.perform(post("/api/v1/auth/logout")
                        .header("X-Client", "web")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "refresh-xxx")))
                .andExpect(status().isNoContent())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().maxAge("refreshToken", 0))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().secure("refreshToken", true));
        verify(authService).logout(any());
    }
}
