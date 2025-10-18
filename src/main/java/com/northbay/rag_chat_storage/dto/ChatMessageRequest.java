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
    
    
    public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
