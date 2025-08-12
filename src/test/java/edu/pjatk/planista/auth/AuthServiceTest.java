package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.auth.dto.RefreshRequest;
import edu.pjatk.planista.security.JwtService;
import edu.pjatk.planista.security.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtService jwtService;
    private AppUserService userService;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any()))
                .thenAnswer(inv -> inv.getArgument(0, UsernamePasswordAuthenticationToken.class));

        String secret = Base64.getEncoder().encodeToString(
                "super-secret-key-which-is-long-enough-32bytes!!!".getBytes(StandardCharsets.UTF_8));
        jwtService = new JwtService(secret, 60_000, 1_200_000);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        userService = new AppUserService(authenticationManager, refreshTokenRepository, jwtService);
    }

    @Test
    void loginReturnsAccessAndRefreshAndCallsAuthenticate() {
        var req = new LoginRequest("admin", "admin123");
        AuthResponse res = userService.login(req);

        assertThat(res.accessToken()).isNotBlank();
        assertThat(res.refreshToken()).isNotBlank();

        assertThat(jwtService.isAccessToken(res.accessToken())).isTrue();
        assertThat(jwtService.isRefreshToken(res.refreshToken())).isTrue();

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("admin");
        assertThat(captor.getValue().getCredentials()).isEqualTo("admin123");
    }

    @Test
    void refreshExchangesRefreshForNewPair() {
        var login = userService.login(new LoginRequest("alice", "pwd"));
        var res = userService.refresh(new RefreshRequest(login.refreshToken()));

        assertThat(res.accessToken()).isNotBlank();
        assertThat(res.refreshToken()).isNotBlank();
        assertThat(jwtService.isAccessToken(res.accessToken())).isTrue();
        assertThat(jwtService.isRefreshToken(res.refreshToken())).isTrue();
        assertThat(res.refreshToken()).isNotEqualTo(login.refreshToken());
    }

    @Test
    void refreshWithAccessTokenShouldFail() {
        var login = userService.login(new LoginRequest("bob", "pwd"));
        assertThatThrownBy(() ->
                userService.refresh(new RefreshRequest(login.accessToken()))
        ).isInstanceOf(BadCredentialsException.class);
    }
}
