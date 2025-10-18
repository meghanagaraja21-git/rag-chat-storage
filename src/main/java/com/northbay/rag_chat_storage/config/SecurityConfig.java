package com.northbay.rag_chat_storage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private ApiKeyAuthFilter apiKeyAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(
                             "/v3/api-docs/**","/v3/openapi.yaml",
                             "/swagger-ui/**",
                             "/swagger-ui.html",
                             "/actuator/**"
                     ).permitAll()
                     
                .anyRequest().authenticated()
            )
            .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}