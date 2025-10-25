package com.northbay.rag_chat_storage.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.repository.ChatMessageRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository messageRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private ChatSession session;
    private ChatMessage message;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        session = new ChatSession();
        session.setId(UUID.randomUUID());
        session.setName("Test Session");

        message = new ChatMessage();
        message.setId(UUID.randomUUID());
        message.setSession(session);
        message.setSender("user");
        message.setContent("Hello world");
        message.setContext("context data");
    }

    @Test
    public void testAddMessage() {
        when(messageRepository.save(any(ChatMessage.class))).thenReturn(message);

        ChatMessage result = chatMessageService.addMessage(session, "user", "Hello world", "context data");

        assertNotNull(result);
        assertEquals(message.getId(), result.getId());
        assertEquals(session, result.getSession());
        assertEquals("user", result.getSender());
        assertEquals("Hello world", result.getContent());
        assertEquals("context data", result.getContext());

        verify(messageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    public void testGetMessages() {
        Page<ChatMessage> page = new PageImpl<>(List.of(message));
        when(messageRepository.findBySession(eq(session), any(PageRequest.class))).thenReturn(page);

        Page<ChatMessage> result = chatMessageService.getMessages(session, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(message, result.getContent().get(0));

        verify(messageRepository, times(1)).findBySession(eq(session), any(PageRequest.class));
    }
}
