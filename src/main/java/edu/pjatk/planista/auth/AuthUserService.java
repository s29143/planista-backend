package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.AuthResponse;
import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.shared.dto.UserDto;
import edu.pjatk.planista.config.security.Jti;
import edu.pjatk.planista.config.security.JwtService;
import edu.pjatk.planista.config.security.RefreshToken;
import edu.pjatk.planista.config.security.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuthRepository authRepository;

    public AuthResponse login(LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        authenticationManager.authenticate(authToken);
        String accessToken = jwtService.generateAccessToken(req.username());
        String refreshToken = jwtService.generateRefreshToken(req.username());
        String jti = jwtService.extractJti(refreshToken);
        String jtiHash = Jti.sha256(jti);
        Instant expiresAt = jwtService.extractExpiration(refreshToken);
        refreshTokenRepository.save(new RefreshToken(jtiHash, req.username(), expiresAt, false));
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token type");
        }
        var username = jwtService.extractUsername(refreshToken);
        String jti = jwtService.extractJti(refreshToken);
        String jtiHash = Jti.sha256(jti);
        RefreshToken rt = refreshTokenRepository.findByJtiHash(jtiHash)
                .orElseThrow(() -> new BadCredentialsException("Unknown refresh token"));
        if(rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token is expired");
        }
        if (!jwtService.isTokenValid(refreshToken, username) || rt.isRevoked()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String newAccess = jwtService.generateAccessToken(username);
        String newRefresh = jwtService.generateRefreshToken(username);
        String newJtiHash = Jti.sha256(jwtService.extractJti(newRefresh));
        Instant expiresAt = jwtService.extractExpiration(newRefresh);

        rt.setRevoked(true);
        refreshTokenRepository.save(rt);
        refreshTokenRepository.save(new RefreshToken(newJtiHash, rt.getUsername(), expiresAt, false));

        return new AuthResponse(newAccess, newRefresh);
    }

    public void logout(String refreshToken) {
        String jti = jwtService.extractJti(refreshToken);
        String jtiHash = Jti.sha256(jti);
        RefreshToken rt = refreshTokenRepository.findByJtiHash(jtiHash)
                .orElseThrow(() -> new BadCredentialsException("Unknown refresh token"));
        rt.setRevoked(true);
        refreshTokenRepository.save(rt);
    }

    public UserDto me(String username) {
        var user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole()
        );
    }
}
