package com.northbay.rag_chat_storage.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Rag Chat Storage API",
        version = "1.0.0",
        description = "REST APIs for managing chat sessions and messages",
        contact = @Contact(name = "NorthBay Team", email = "support@northbay.com")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local server")
    }
)
public class OpenApiConfig {
    // No methods required, annotations are enough
}