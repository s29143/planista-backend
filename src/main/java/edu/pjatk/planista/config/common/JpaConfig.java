package edu.pjatk.planista.config.common;

import edu.pjatk.planista.shared.models.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> {
            var ctx = SecurityContextHolder.getContext();
            var auth = (ctx != null) ? ctx.getAuthentication() : null;

            if (auth == null || !auth.isAuthenticated()
                    || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
                return Optional.empty();
            }
            return Optional.ofNullable(extractUserId(auth.getPrincipal()));
        };
    }

    private Long extractUserId(Object principal) {
         return ((AppUser) principal).getId();
    }
}