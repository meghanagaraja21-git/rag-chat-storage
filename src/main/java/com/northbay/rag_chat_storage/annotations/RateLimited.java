package com.northbay.rag_chat_storage.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimited {
    /**
     * Name of the rate limiter (matches configuration in application.properties)
     */
    String name();
}