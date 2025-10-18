package com.northbay.rag_chat_storage.config;


import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class RequestResponseLoggingFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String traceId = UUID.randomUUID().toString();
        httpReq.setAttribute("traceId", traceId);
        long start = System.currentTimeMillis();
        log.info("[TRACE_ID: {}] Incoming Request: {} {}", traceId, httpReq.getMethod(), httpReq.getRequestURI());

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - start;
        log.info("[TRACE_ID: {}] Response: {} | Time Taken: {} ms", traceId, httpRes.getStatus(), duration);
  }
}
