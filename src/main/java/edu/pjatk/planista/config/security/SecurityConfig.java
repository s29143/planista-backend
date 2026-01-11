package edu.pjatk.planista.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Objects;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(CorsProps.class)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthRateLimitFilter authRateLimitFilter;
    public static final String[] PUBLIC = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout",
            "/api/v1/auth/me",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(SecurityConfig.PUBLIC).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/dict/**").authenticated()
                        .requestMatchers("/api/v1/dict/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authRateLimitFilter, SecurityContextHolderFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProps props) {
        CorsConfiguration cfg = new CorsConfiguration();

        if (props.allowedOrigins() != null && !props.allowedOrigins().isEmpty()) {
            cfg.setAllowedOrigins(props.allowedOrigins());
        }
        if (props.allowedOriginPatterns() != null && !props.allowedOriginPatterns().isEmpty()) {
            cfg.setAllowedOriginPatterns(props.allowedOriginPatterns());
        }
        cfg.setAllowedMethods(Objects.requireNonNullElseGet(props.allowedMethods(), () -> java.util.List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS")));
        cfg.setAllowedHeaders(Objects.requireNonNullElseGet(props.allowedHeaders(), () -> java.util.List.of("*")));
        if (props.exposedHeaders() != null) cfg.setExposedHeaders(props.exposedHeaders());
        cfg.setAllowCredentials(Boolean.TRUE.equals(props.allowCredentials()));
        if (props.maxAge() != null) cfg.setMaxAge(props.maxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
