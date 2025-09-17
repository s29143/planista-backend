package edu.pjatk.planista.config;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.auth.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
public class JpaConfig {

    private final AppUserRepository appUserRepository;

    @Bean
    public AuditorAware<AppUser> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .flatMap(appUserRepository::findByUsername);
    }
}
