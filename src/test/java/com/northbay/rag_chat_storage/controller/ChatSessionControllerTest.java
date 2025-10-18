package com.northbay.rag_chat_storage.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import com.northbay.rag_chat_storage.dto.CreateSessionRequest;
import com.northbay.rag_chat_storage.dto.FavoriteSessionRequest;
import com.northbay.rag_chat_storage.dto.RenameSessionRequest;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.service.ChatSessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ChatSessionControllerTest {

    @Mock
    private ChatSessionService sessionService;

    @InjectMocks
    private ChatSessionController chatSessionController;

    private UUID sessionId;
    private ChatSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionId = UUID.randomUUID();
        session = new ChatSession();
        session.setId(sessionId);
        session.setName("Test Session");
    }

    @Test
    public void testCreateSession() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setUserId("user123");
        request.setName("New Session");

        when(sessionService.createSession(request.getUserId(), request.getName())).thenReturn(session);

        ResponseEntity<ChatSession> response = chatSessionController.createSession(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(session, response.getBody());

        verify(sessionService, times(1)).createSession(request.getUserId(), request.getName());
    }

    @Test
    public void testGetSession() {
        when(sessionService.getSession(sessionId)).thenReturn(session);

        ResponseEntity<ChatSession> response = chatSessionController.getSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());

        verify(sessionService, times(1)).getSession(sessionId);
    }

    @Test
    public void testGetAllSessions() {
        List<ChatSession> sessions = List.of(session);
        when(sessionService.getAllSessions(null)).thenReturn(sessions);

        ResponseEntity<List<ChatSession>> response = chatSessionController.getAllSessions(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessions, response.getBody());

        verify(sessionService, times(1)).getAllSessions(null);
    }

    @Test
    public void testRenameSession() {
        RenameSessionRequest request = new RenameSessionRequest();
        request.setName("Renamed Session");

        when(sessionService.renameSession(sessionId, request.getName())).thenReturn(session);

        ResponseEntity<ChatSession> response = chatSessionController.renameSession(sessionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());

        verify(sessionService, times(1)).renameSession(sessionId, request.getName());
    }

    @Test
    public void testMarkFavorite() {
        FavoriteSessionRequest request = new FavoriteSessionRequest();
        request.setFavorite(true);

        when(sessionService.markFavorite(sessionId, request.isFavorite())).thenReturn(session);

        ResponseEntity<ChatSession> response = chatSessionController.markFavorite(sessionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());

        verify(sessionService, times(1)).markFavorite(sessionId, request.isFavorite());
    }

    @Test
    public void testDeleteSession() {
        doNothing().when(sessionService).deleteSession(sessionId);

        ResponseEntity<Void> response = chatSessionController.deleteSession(sessionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(sessionService, times(1)).deleteSession(sessionId);
    }
}
