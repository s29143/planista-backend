package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.AuthResponse;
import edu.pjatk.planista.auth.dto.LoginRequest;
import edu.pjatk.planista.auth.dto.MeResponse;
import edu.pjatk.planista.auth.dto.RefreshRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AppUserService userService;
    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpMs;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req,
                                              @RequestHeader(value="X-Client", required=false) String client,
                                              HttpServletResponse resp) {
        boolean isWeb = "web".equalsIgnoreCase(client);
        AuthResponse authResponse = userService.login(req);
        if (isWeb) {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", authResponse.refreshToken())
                    .httpOnly(true).secure(true).sameSite("None").path("/auth").maxAge(refreshExpMs / 1000).build();
            resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            authResponse = new AuthResponse(authResponse.accessToken(), "");
        }
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody(required = false) RefreshRequest req,
                                                @RequestHeader(value="X-Client", required=false) String client,
                                                @CookieValue(value="refreshToken", required=false) String cookieRefresh,
                                                HttpServletResponse resp) {
        boolean isWeb = "web".equalsIgnoreCase(client);
        String refresh = isWeb ? cookieRefresh : ( req != null ? req.refreshToken(): null);
        AuthResponse authResponse = userService.refresh(refresh);
        if (isWeb) {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", authResponse.refreshToken())
                    .httpOnly(true).secure(true).sameSite("None").path("/auth").maxAge(refreshExpMs / 1000).build();
            resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            authResponse = new AuthResponse(authResponse.accessToken(), "");
        }
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserDetails user) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var response = userService.me(user.getUsername());
        return ResponseEntity.ok(response);
    }
}
