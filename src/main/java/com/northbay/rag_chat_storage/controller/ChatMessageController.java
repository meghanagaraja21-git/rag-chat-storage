package com.northbay.rag_chat_storage.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.northbay.rag_chat_storage.annotations.RateLimited;
import com.northbay.rag_chat_storage.dto.ChatMessageRequest;
import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.service.ChatMessageService;
import com.northbay.rag_chat_storage.service.ChatService;
import com.northbay.rag_chat_storage.service.ChatSessionService;

import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
@RateLimited(name = "placeholder")
@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/messages")

public class ChatMessageController {

    @Autowired
    private  ChatMessageService messageService;

    @Autowired
    private  ChatSessionService sessionService;
    
    @Autowired
    private ChatService chatService; 

    /**
     * Add a new message to a chat session.
     */
    
    @PostMapping
    public ResponseEntity<List<ChatMessage>> addMessage(
            @PathVariable UUID sessionId,
            @Valid @RequestBody ChatMessageRequest request) {

    	
    	
        ChatSession session = sessionService.getSession(sessionId);
        ChatMessage savedMessage = messageService.addMessage(
                session,
                "user",
                request.getContent(),
                request.getContext());
        
    
        
        String aiReply = chatService.generateResponse(
                request.getContent(),  // user input
                request.getContext()   // context for AI instructions
        );

        // 4️⃣ Save AI (assistant) message
        ChatMessage aiMessage = messageService.addMessage(
                session,
                "assistant",
                aiReply,
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(Arrays.asList(savedMessage, aiMessage));
    }
    /**
     * Fetch paginated chat messages for a session.
     */
    
    @GetMapping
    public ResponseEntity<Page<ChatMessage>> getMessages(
            @PathVariable UUID sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ChatSession session = sessionService.getSession(sessionId);
        Page<ChatMessage> messages = messageService.getMessages(session, PageRequest.of(page, size));

        return ResponseEntity.ok(messages);
    }
    
  
    
}
