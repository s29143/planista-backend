package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.AuthResponse;
import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.shared.dto.UserDto;
import edu.pjatk.planista.config.security.Jti;
import edu.pjatk.planista.config.security.JwtService;
import edu.pjatk.planista.config.security.RefreshToken;
import edu.pjatk.planista.config.security.RefreshTokenRepository;
import edu.pjatk.planista.shared.models.AppUser;
import edu.pjatk.planista.shared.mappers.AppUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtService jwtService;
    private AuthUserService userService;
    private AuthRepository authRepository;
    private AppUserMapper mapper;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any()))
                .thenAnswer(inv -> inv.getArgument(0, UsernamePasswordAuthenticationToken.class));

        String secret = Base64.getEncoder().encodeToString(
                "super-secret-key-which-is-long-enough-32bytes!!!".getBytes(StandardCharsets.UTF_8));
        jwtService = new JwtService(secret, Duration.of(60_000, ChronoUnit.MILLIS), Duration.of(60 * 60 * 1000 - 1000, ChronoUnit.MILLIS));
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        authRepository = mock(AuthRepository.class);
        userService = new AuthUserService(authenticationManager, refreshTokenRepository, jwtService, authRepository);
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

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken savedAtLogin = captor.getValue();

        when(refreshTokenRepository.findByJtiHash(savedAtLogin.getJtiHash()))
                .thenReturn(java.util.Optional.of(
                        new RefreshToken(savedAtLogin.getJtiHash(),
                                savedAtLogin.getUsername(),
                                savedAtLogin.getExpiresAt(),
                                false)
                ));

        var res = userService.refresh(login.refreshToken());


        assertThat(res.accessToken()).isNotBlank();
        assertThat(res.refreshToken()).isNotBlank();
        assertThat(jwtService.isAccessToken(res.accessToken())).isTrue();
        assertThat(jwtService.isRefreshToken(res.refreshToken())).isTrue();
        assertThat(res.refreshToken()).isNotEqualTo(login.refreshToken());

        verify(refreshTokenRepository, times(3)).save(any(RefreshToken.class));
        verify(refreshTokenRepository).findByJtiHash(savedAtLogin.getJtiHash());
    }

    @Test
    void refreshWithUnknownJtiFails() {
        var login = userService.login(new LoginRequest("eve", "pwd"));

        when(refreshTokenRepository.findByJtiHash(any()))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userService.refresh(login.refreshToken()))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refreshReuseShouldFail() {
        var login = userService.login(new LoginRequest("kate", "pwd"));

        ArgumentCaptor<RefreshToken> cap = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(cap.capture());
        var saved = cap.getValue();

        when(refreshTokenRepository.findByJtiHash(saved.getJtiHash()))
                .thenReturn(java.util.Optional.of(
                        new RefreshToken(saved.getJtiHash(), saved.getUsername(),
                                saved.getExpiresAt(), true)
                ));

        assertThatThrownBy(() -> userService.refresh(login.refreshToken()))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refreshExpiredShouldFail() {
        var login = userService.login(new LoginRequest("max", "pwd"));

        ArgumentCaptor<RefreshToken> cap = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(cap.capture());
        var saved = cap.getValue();

        when(refreshTokenRepository.findByJtiHash(saved.getJtiHash()))
                .thenReturn(java.util.Optional.of(
                        new RefreshToken(saved.getJtiHash(), saved.getUsername(),
                                Instant.now().minus(5, ChronoUnit.HOURS),
                                false)
                ));

        assertThatThrownBy(() -> userService.refresh(login.refreshToken()))
                .isInstanceOf(BadCredentialsException.class).hasMessageContaining("expired");
    }

    @Test
    void meReturnsDtoFromRepository() {
        AppUser user = new AppUser();
        user.setId(7L);
        user.setUsername("jan");
        user.setFirstname("Jan");
        user.setLastname("Kowalski");

        when(authRepository.findByUsername("jan")).thenReturn(Optional.of(user));

        UserDto res = userService.me("jan");

        assertThat(res.id()).isEqualTo(7L);
        assertThat(res.username()).isEqualTo("jan");
        assertThat(res.firstname()).isEqualTo("Jan");
        assertThat(res.lastname()).isEqualTo("Kowalski");
        verify(authRepository).findByUsername("jan");
        verifyNoMoreInteractions(authRepository);
    }

    @Test
    void meThrowsWhenUserNotFound() {
        when(authRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.me("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(authRepository).findByUsername("ghost");
        verifyNoMoreInteractions(authRepository);
    }

    @Test
    void logoutSetsRevoked() {
        String token = jwtService.generateRefreshToken("john");
        String jti = jwtService.extractJti(token);
        String jtiHash = Jti.sha256(jti);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(false);
        when(refreshTokenRepository.findByJtiHash(jtiHash)).thenReturn(Optional.of(refreshToken));

        userService.logout(token);

        verify(refreshTokenRepository).findByJtiHash(jtiHash);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken saved = captor.getValue();
        assertThat(saved.isRevoked()).isTrue();
        verifyNoMoreInteractions(refreshTokenRepository);
    }
}
