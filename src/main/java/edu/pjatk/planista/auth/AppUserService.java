package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.auth.dto.RefreshRequest;
import edu.pjatk.planista.security.Jti;
import edu.pjatk.planista.security.JwtService;
import edu.pjatk.planista.security.RefreshToken;
import edu.pjatk.planista.security.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        authenticationManager.authenticate(authToken);
        String accessToken = jwtService.generateAccessToken(req.username());
        String refreshToken = jwtService.generateRefreshToken(req.username());
        String jti = jwtService.extractJti(refreshToken);
        String jtiHash = Jti.sha256(jti);
        Instant expiresAt = jwtService.extractExpiration(refreshToken);
        refreshTokenRepository.save(new RefreshToken(jtiHash, req.username(), expiresAt, false));
        return new AuthResponse("Bearer " + accessToken, "Bearer " + refreshToken);
    }

    public AuthResponse refresh(RefreshRequest req) {
        String refreshToken = req.refreshToken();
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token type");
        }
        var username = jwtService.extractUsername(refreshToken);
        String jti = jwtService.extractJti(refreshToken);
        String jtiHash = Jti.sha256(jti);
        RefreshToken rt = refreshTokenRepository.findByJtiHash(jtiHash)
                .orElseThrow(() -> new BadCredentialsException("Unknown refresh token"));

        if (!jwtService.isTokenValid(refreshToken, username) || rt.isRevoked()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String newAccess = jwtService.generateAccessToken(username);
        String newRefresh = jwtService.generateRefreshToken(username);
        String newJtiHash = Jti.sha256(jwtService.extractJti(newRefresh));
        Instant expiresAt = jwtService.extractExpiration(refreshToken);

        rt.setRevoked(true);
        refreshTokenRepository.save(rt);
        refreshTokenRepository.save(new RefreshToken(newJtiHash, rt.getUsername(), expiresAt, false));

        return new AuthResponse(newAccess, newRefresh);
    }
}
