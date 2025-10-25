package com.northbay.rag_chat_storage.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.northbay.rag_chat_storage.annotations.RateLimited;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.http.HttpServletRequest;


@Aspect
@Component("customRateLimiterAspect")
public class RateLimiterAspect {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Around("@annotation(rateLimited) || @within(rateLimited)")
    public Object aroundRateLimitedMethod(ProceedingJoinPoint pjp, RateLimited rateLimited) throws Throwable {
        
        // Get the API key from request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Missing API key");
        }

        // Use API key as limiter name
        RateLimiter limiter = rateLimiterRegistry.rateLimiter(apiKey);

        if (!limiter.acquirePermission()) {
            if (pjp.getSignature().getDeclaringType().getName().contains("Controller")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Rate limit exceeded for API key: " + apiKey);
            }
            throw new RuntimeException("Rate limit exceeded for API key: " + apiKey);
        }

        return pjp.proceed();
    }
}