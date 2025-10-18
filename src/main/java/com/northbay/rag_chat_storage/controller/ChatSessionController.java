package com.northbay.rag_chat_storage.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import lombok.RequiredArgsConstructor;


/**
 * REST Controller for managing Chat Sessions.
 * 
 * Responsibilities:
 *  - Create, retrieve, rename, favorite, and delete chat sessions
 *  - Delegates all logic to ChatSessionService
 *  - Applies consistent HTTP status codes and response structure
 */
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    @Autowired
    private  ChatSessionService sessionService;

    @RateLimited(name = "chatSessionLimiter")
    @PostMapping
    public ResponseEntity<ChatSession> createSession(@Valid @RequestBody CreateSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createSession(request.getUserId(), request.getName()));
    }

    @RateLimited(name = "chatSessionLimiter")
    @GetMapping("/{id}")
    public ResponseEntity<ChatSession> getSession(@PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.getSession(id));
    }

    @RateLimited(name = "chatSessionLimiter")
    @GetMapping
    public ResponseEntity<List<ChatSession>> getAllSessions(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(sessionService.getAllSessions(userId));
    }

    @RateLimited(name = "chatSessionLimiter")
    @PutMapping("/{id}/rename")
    public ResponseEntity<ChatSession> renameSession(@PathVariable UUID id,
                                                     @Valid @RequestBody RenameSessionRequest request) {
        return ResponseEntity.ok(sessionService.renameSession(id, request.getName()));
    }

    @RateLimited(name = "chatSessionLimiter")
    @PutMapping("/{id}/favorite")
    public ResponseEntity<ChatSession> markFavorite(@PathVariable UUID id,
                                                    @Valid @RequestBody FavoriteSessionRequest request) {
        return ResponseEntity.ok(sessionService.markFavorite(id, request.isFavorite()));
    }

    @RateLimited(name = "chatSessionLimiter")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
