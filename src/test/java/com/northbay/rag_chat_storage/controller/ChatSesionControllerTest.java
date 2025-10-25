package com.northbay.rag_chat_storage.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.northbay.rag_chat_storage.dto.CreateSessionRequest;
import com.northbay.rag_chat_storage.dto.FavoriteSessionRequest;
import com.northbay.rag_chat_storage.dto.RenameSessionRequest;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.service.ChatSessionService;

class ChatSessionControllerTest {

    @Mock
    private ChatSessionService sessionService;

    @InjectMocks
    private ChatSessionController controller;

    private UUID sessionId;
    private ChatSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionId = UUID.randomUUID();
        session = new ChatSession();
        session.setId(sessionId);
        session.setName("Session 1");
    }

    /**
     *  Test: Create new session
     */
    @Test
    void testCreateSession_Success() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setUserId("user123");
        request.setName("My First Session");

        when(sessionService.createSession("user123", "My First Session")).thenReturn(session);

        ResponseEntity<ChatSession> response = controller.createSession(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(session, response.getBody());
        verify(sessionService, times(1)).createSession("user123", "My First Session");
    }

    /**
     *  Test: Get session by ID
     */
    @Test
    void testGetSession_Success() {
        when(sessionService.getSession(sessionId)).thenReturn(session);

        ResponseEntity<ChatSession> response = controller.getSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());
        verify(sessionService, times(1)).getSession(sessionId);
    }

    /**
     * Test: Get all sessions for a user
     */
    @Test
    void testGetAllSessions_Success() {
        ChatSession s1 = new ChatSession();
        ChatSession s2 = new ChatSession();
        List<ChatSession> sessions = Arrays.asList(s1, s2);

        when(sessionService.getAllSessions("user123")).thenReturn(sessions);

        ResponseEntity<List<ChatSession>> response = controller.getAllSessions("user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(sessionService, times(1)).getAllSessions("user123");
    }

    /**
     *  Test: Rename session
     */
    @Test
    void testRenameSession_Success() {
        RenameSessionRequest request = new RenameSessionRequest();
        request.setName("New Name");

        ChatSession renamedSession = new ChatSession();
        renamedSession.setId(sessionId);
        renamedSession.setName("New Name");

        when(sessionService.renameSession(sessionId, "New Name")).thenReturn(renamedSession);

        ResponseEntity<ChatSession> response = controller.renameSession(sessionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Name", response.getBody().getName());
        verify(sessionService, times(1)).renameSession(sessionId, "New Name");
    }

    /**
     * Test: Mark session as favorite
     */
    @Test
    void testMarkFavorite_Success() {
        FavoriteSessionRequest request = new FavoriteSessionRequest();
        request.setFavorite(true);

        ChatSession favoriteSession = new ChatSession();
        favoriteSession.setId(sessionId);
        favoriteSession.setFavorite(true);

        when(sessionService.markFavorite(sessionId, true)).thenReturn(favoriteSession);

        ResponseEntity<ChatSession> response = controller.markFavorite(sessionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isFavorite());
        verify(sessionService, times(1)).markFavorite(sessionId, true);
    }

    /**
     *  Test: Delete session
     */
    @Test
    void testDeleteSession_Success() {
        doNothing().when(sessionService).deleteSession(sessionId);

        ResponseEntity<Void> response = controller.deleteSession(sessionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(sessionService, times(1)).deleteSession(sessionId);
    }
}
