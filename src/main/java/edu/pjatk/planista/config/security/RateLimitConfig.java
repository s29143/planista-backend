package edu.pjatk.planista.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.config.filters.AuthRateLimitFilter;
import edu.pjatk.planista.config.filters.IntegrationRateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class RateLimitConfig {

    @Bean
    public AuthRateLimitFilter authRateLimitFilter(HandlerExceptionResolver handlerExceptionResolver, ObjectMapper objectMapper) {
        return new AuthRateLimitFilter(handlerExceptionResolver, objectMapper);
    }
    @Bean
    public IntegrationRateLimitFilter integrationRateLimitFilter(HandlerExceptionResolver handlerExceptionResolver) {
        return new IntegrationRateLimitFilter(handlerExceptionResolver);
    }
}
