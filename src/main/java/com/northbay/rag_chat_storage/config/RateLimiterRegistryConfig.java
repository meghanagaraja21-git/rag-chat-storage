package com.northbay.rag_chat_storage.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Configuration
public class RateLimiterRegistryConfig {

    @Value("${api.key}")
    private String apiKeys;

    @Value("${rate.limiter.default.limit:3}")
    private int defaultLimit;

    @Value("${rate.limiter.default.period:10}")
    private int defaultPeriod;

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterRegistry registry = RateLimiterRegistry.ofDefaults();
        initApiKeyLimiters(registry);
        return registry;
    }

    private void initApiKeyLimiters(RateLimiterRegistry registry) {
        for (String key : apiKeys.split(",")) {
            String trimmedKey = key.trim();

            int limit = getConfigValue("rate.limiter." + trimmedKey + ".limit", defaultLimit);
            int period = getConfigValue("rate.limiter." + trimmedKey + ".period", defaultPeriod);

            RateLimiterConfig config = RateLimiterConfig.custom()
                    .limitForPeriod(limit)
                    .limitRefreshPeriod(Duration.ofSeconds(period))
                    .timeoutDuration(Duration.ofMillis(0))
                    .build();

            registry.rateLimiter(trimmedKey, config);
            System.out.printf("Rate limiter created for %s: %d requests per %ds%n", trimmedKey, limit, period);
        }
    }

    private int getConfigValue(String property, int defaultValue) {
        String value = System.getProperty(property);
        if (value == null) {
            value = System.getenv(property.replace('.', '_').toUpperCase());
        }
        try {
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
