package edu.pjatk.planista.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public AuthRateLimitFilter authRateLimitFilter(ObjectMapper objectMapper) {
        return new AuthRateLimitFilter(objectMapper);
    }
}
