package edu.pjatk.planista.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class RateLimitConfig {

    @Bean
    public AuthRateLimitFilter authRateLimitFilter(HandlerExceptionResolver handlerExceptionResolver, ObjectMapper objectMapper) {
        return new AuthRateLimitFilter(handlerExceptionResolver, objectMapper);
    }
}
