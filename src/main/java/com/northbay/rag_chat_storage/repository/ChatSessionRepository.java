package com.northbay.rag_chat_storage.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northbay.rag_chat_storage.models.ChatSession;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
}