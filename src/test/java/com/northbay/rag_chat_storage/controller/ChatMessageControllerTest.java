package com.northbay.rag_chat_storage.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.northbay.rag_chat_storage.dto.ChatMessageRequest;
import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.service.ChatMessageService;
import com.northbay.rag_chat_storage.service.ChatService;
import com.northbay.rag_chat_storage.service.ChatSessionService;

class ChatMessageControllerTest {

    @Mock
    private ChatMessageService messageService;

    @Mock
    private ChatSessionService sessionService;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatMessageController controller;

    private UUID sessionId;
    private ChatSession session;
    private ChatMessageRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionId = UUID.randomUUID();
        session = new ChatSession();
        request = new ChatMessageRequest();
        request.setContent("Hello AI");
        request.setContext("General chat");
    }

    /**
     * ✅ Test: Add user message and AI reply successfully
     */
    @Test
    void testAddMessage_Success() {
        ChatMessage userMessage = new ChatMessage();

        userMessage.setContent("Hello AI");

        ChatMessage aiMessage = new ChatMessage();
  
        aiMessage.setContent("Hi, how can I help?");

        // Mock dependencies
        when(sessionService.getSession(sessionId)).thenReturn(session);
        when(messageService.addMessage(eq(session), eq("user"), anyString(), anyString())).thenReturn(userMessage);
        when(chatService.generateResponse(anyString(), anyString())).thenReturn("Hi, how can I help?");
        when(messageService.addMessage(eq(session), eq("assistant"), anyString(), isNull())).thenReturn(aiMessage);

        // Call controller method
        ResponseEntity<List<ChatMessage>> response = controller.addMessage(sessionId, request);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, response.getBody().size());
  
        
        verify(sessionService, times(1)).getSession(sessionId);
        verify(messageService, times(2)).addMessage(any(), anyString(), anyString(), any());
        verify(chatService, times(1)).generateResponse(anyString(), anyString());
    }

    /**
     * ✅ Test: Get paginated chat messages successfully
     */
    @Test
    void testGetMessages_Success() {
        ChatMessage msg1 = new ChatMessage();
        msg1.setContent("Hello");
        ChatMessage msg2 = new ChatMessage();
        msg2.setContent("Hi there!");

        Page<ChatMessage> page = new PageImpl<>(Arrays.asList(msg1, msg2));

        when(sessionService.getSession(sessionId)).thenReturn(session);
        when(messageService.getMessages(eq(session), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<ChatMessage>> response = controller.getMessages(sessionId, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Hello", response.getBody().getContent().get(0).getContent());

        verify(sessionService, times(1)).getSession(sessionId);
        verify(messageService, times(1)).getMessages(eq(session), any(PageRequest.class));
    }
}
