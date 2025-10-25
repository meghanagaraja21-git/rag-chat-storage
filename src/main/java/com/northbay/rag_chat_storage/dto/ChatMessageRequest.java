package com.northbay.rag_chat_storage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatMessageRequest {

    @NotBlank(message = "Sender is required")
    private String sender;

	@NotBlank(message = "Content cannot be empty")
    private String content;

    private String context; // optional JSON/context string
}
