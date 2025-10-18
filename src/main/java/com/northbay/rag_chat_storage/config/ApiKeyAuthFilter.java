package com.northbay.rag_chat_storage.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${api.key}")
    private String apiKeysProperty;

    private List<String> validApiKeys;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        if (!StringUtils.hasText(apiKeysProperty)) {
            throw new ServletException("No API keys defined in 'api.keys'");
        }
        validApiKeys = Arrays.asList(apiKeysProperty.split(","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader("X-API-KEY");
        System.out.println("Incoming API Key: " + requestApiKey);

        if (StringUtils.hasText(requestApiKey) && validApiKeys.contains(requestApiKey.trim())) {
            // Set authenticated user in SecurityContext
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            "apiKeyUser", // principal
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_API_USER"))
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Invalid or missing API key");
        }
    }
}