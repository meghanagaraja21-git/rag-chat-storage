package com.northbay.rag_chat_storage.config;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterRegistryConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        // Create a default configuration for the rate limiter
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5) // 5 requests
                .limitRefreshPeriod(Duration.ofSeconds(10)) // per 10 seconds
                .timeoutDuration(Duration.ofMillis(0))
                .build();

        return RateLimiterRegistry.of(config);
    }
}