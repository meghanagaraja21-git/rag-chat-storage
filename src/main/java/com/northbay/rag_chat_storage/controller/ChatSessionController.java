package com.northbay.rag_chat_storage.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.northbay.rag_chat_storage.annotations.RateLimited;
import com.northbay.rag_chat_storage.dto.CreateSessionRequest;
import com.northbay.rag_chat_storage.dto.FavoriteSessionRequest;
import com.northbay.rag_chat_storage.dto.RenameSessionRequest;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.service.ChatSessionService;

import jakarta.validation.Valid;


/**
 * REST Controller for managing Chat Sessions.
 * 
 * Responsibilities:
 *  - Create, retrieve, rename, favorite, and delete chat sessions
 *  - Delegates all logic to ChatSessionService
 *  - Applies consistent HTTP status codes and response structure
 */
@RateLimited(name = "placeholder")
@RestController
@RequestMapping("/api/v1/sessions")
public class ChatSessionController {

    @Autowired
    private ChatSessionService sessionService;

    @PostMapping
    public ResponseEntity<ChatSession> createSession(@Valid @RequestBody CreateSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createSession(request.getUserId(), request.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatSession> getSession(@PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.getSession(id));
    }

    @GetMapping
    public ResponseEntity<List<ChatSession>> getAllSessions(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(sessionService.getAllSessions(userId));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<ChatSession> renameSession(@PathVariable UUID id,
                                                     @Valid @RequestBody RenameSessionRequest request) {
        return ResponseEntity.ok(sessionService.renameSession(id, request.getName()));
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ChatSession> markFavorite(@PathVariable UUID id,
                                                    @Valid @RequestBody FavoriteSessionRequest request) {
        return ResponseEntity.ok(sessionService.markFavorite(id, request.isFavorite()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
