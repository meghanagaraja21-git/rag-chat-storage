package com.northbay.rag_chat_storage.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.northbay.rag_chat_storage.exceptions.NotFoundException;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.repository.ChatSessionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository sessionRepository;

    @InjectMocks
    private ChatSessionService chatSessionService;

    private UUID sessionId;
    private ChatSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionId = UUID.randomUUID();

        session = new ChatSession();
        session.setId(sessionId);
        session.setUserId("user123");
        session.setName("Test Session");
        session.setFavorite(false);
    }

    @Test
    public void testCreateSession() {
        when(sessionRepository.save(any(ChatSession.class))).thenReturn(session);

        ChatSession result = chatSessionService.createSession("user123", "Test Session");

        assertNotNull(result);
        assertEquals("Test Session", result.getName());
        assertEquals("user123", result.getUserId());
        assertFalse(result.isFavorite());

        verify(sessionRepository, times(1)).save(any(ChatSession.class));
    }

    @Test
    public void testGetSession() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        ChatSession result = chatSessionService.getSession(sessionId);

        assertNotNull(result);
        assertEquals(sessionId, result.getId());

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testGetSessionNotFound() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chatSessionService.getSession(sessionId));

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testRenameSession() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        ChatSession result = chatSessionService.renameSession(sessionId, "Renamed");

        assertEquals("Renamed", result.getName());
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testMarkFavorite() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        ChatSession result = chatSessionService.markFavorite(sessionId, true);

        assertTrue(result.isFavorite());
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDeleteSession() {
        when(sessionRepository.existsById(sessionId)).thenReturn(true);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        doNothing().when(sessionRepository).delete(session);

        chatSessionService.deleteSession(sessionId);

        verify(sessionRepository, times(1)).existsById(sessionId);
        verify(sessionRepository, times(1)).delete(session);
    }

    @Test
    public void testDeleteSessionNotFound() {
        when(sessionRepository.existsById(sessionId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> chatSessionService.deleteSession(sessionId));

        verify(sessionRepository, times(1)).existsById(sessionId);
        verify(sessionRepository, times(0)).delete(any());
    }

    @Test
    public void testGetAllSessions() {
        List<ChatSession> sessions = List.of(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<ChatSession> result = chatSessionService.getAllSessions(null);

        assertEquals(1, result.size());
        assertEquals(session, result.get(0));

        verify(sessionRepository, times(1)).findAll();
    }
}
