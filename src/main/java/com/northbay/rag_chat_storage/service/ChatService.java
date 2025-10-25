package com.northbay.rag_chat_storage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

	  @Autowired
    private final OllamaApi ollamaApi;

  
    public ChatService(OllamaApi ollamaApi) {
        this.ollamaApi = ollamaApi;
    }

    /**
     * Generate AI response using a user message and optional context.
     *
     * @param userMessage the user input message
     * @param context     system-level instructions or context for the AI
     * @return AI-generated response as a String
     */
    public String generateResponse(String userMessage, String context) {
        List<Message> messages = new ArrayList<>();

        // Add system message if context is provided
        if (context != null && !context.isBlank()) {
            messages.add(new Message(Message.Role.SYSTEM, context, null, null));
        }

        // Add user message
        messages.add(new Message(Message.Role.USER, userMessage, null, null));

        ChatRequest request = new ChatRequest(
                "gemma3:4b",messages,false,null,null,null,null);

        ChatResponse response = ollamaApi.chat(request);
        Message assistantMessage = response.message();

        return (assistantMessage != null && assistantMessage.content() != null)
                ? assistantMessage.content().trim()
                : "";
    }
}
