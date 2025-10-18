package com.northbay.rag_chat_storage.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.northbay.rag_chat_storage.annotations.RateLimited;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;


@Aspect
@Component("customRateLimiterAspect") 
public class RateLimiterAspect {
	
@Autowired
    private  RateLimiterRegistry rateLimiterRegistry;

    public RateLimiterAspect(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @Around("@annotation(rateLimited)")
    public Object aroundRateLimitedMethod(ProceedingJoinPoint pjp, RateLimited rateLimited) throws Throwable {
        String limiterName = rateLimited.name();
        RateLimiter limiter = rateLimiterRegistry.rateLimiter(limiterName);

        if (!limiter.acquirePermission()) {
            // For controller methods, return HTTP 429 directly
            if (pjp.getSignature().getDeclaringType().getName().contains("Controller")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Rate limit exceeded. Try again later.");
            }

            // For service methods, throw a custom exception
            throw new RuntimeException("Rate limit exceeded for limiter: " + limiterName);
        }

        return pjp.proceed();
    }

}