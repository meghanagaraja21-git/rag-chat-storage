package com.northbay.rag_chat_storage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreateSessionRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String name;
	
}