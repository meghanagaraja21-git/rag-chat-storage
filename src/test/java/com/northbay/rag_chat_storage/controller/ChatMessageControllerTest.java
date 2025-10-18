package com.northbay.rag_chat_storage.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import com.northbay.rag_chat_storage.dto.ChatMessageRequest;
import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.service.ChatMessageService;
import com.northbay.rag_chat_storage.service.ChatSessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ChatMessageControllerTest {

    @Mock
    private ChatMessageService messageService;

    @Mock
    private ChatSessionService sessionService;

    @InjectMocks
    private ChatMessageController chatMessageController;

    private UUID sessionId;
    private ChatSession session;
    private ChatMessage message;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionId = UUID.randomUUID();
        session = new ChatSession();
        session.setId(sessionId);
        session.setName("Test Session");

        message = new ChatMessage();
        message.setId(UUID.randomUUID());
        message.setSender("user");
        message.setContent("Hello world");
        message.setSession(session);
    }

    @Test
    public void testAddMessage() {
        ChatMessageRequest request = new ChatMessageRequest();
        request.setSender("user");
        request.setContent("Hello world");
        request.setContext("context data");

        when(sessionService.getSession(sessionId)).thenReturn(session);
        when(messageService.addMessage(session, request.getSender(), request.getContent(), request.getContext()))
                .thenReturn(message);

        ResponseEntity<ChatMessage> response = chatMessageController.addMessage(sessionId, request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(message, response.getBody());

        verify(sessionService, times(1)).getSession(sessionId);
        verify(messageService, times(1))
                .addMessage(session, request.getSender(), request.getContent(), request.getContext());
    }

    @Test
    public void testGetMessages() {
        Page<ChatMessage> page = new PageImpl<>(List.of(message));

        when(sessionService.getSession(sessionId)).thenReturn(session);
        when(messageService.getMessages(session, PageRequest.of(0, 10))).thenReturn(page);

        ResponseEntity<Page<ChatMessage>> response = chatMessageController.getMessages(sessionId, 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(page, response.getBody());

        verify(sessionService, times(1)).getSession(sessionId);
        verify(messageService, times(1)).getMessages(session, PageRequest.of(0, 10));
    }
}
