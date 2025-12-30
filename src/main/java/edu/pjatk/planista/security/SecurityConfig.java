package edu.pjatk.planista.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    public static final String[] PUBLIC = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(SecurityConfig.PUBLIC).permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().denyAll()
                )
                .exceptionHandling(c -> c
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(401);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"title\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(403);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"title\":\"Forbidden\"}");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
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
