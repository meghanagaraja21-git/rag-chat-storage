package com.northbay.rag_chat_storage.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findBySession(ChatSession session, Pageable pageable);
    Optional<ChatMessage> findByIdAndSession(UUID id, ChatSession session);

}