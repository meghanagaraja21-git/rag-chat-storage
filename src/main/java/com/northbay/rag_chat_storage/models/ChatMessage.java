package com.northbay.rag_chat_storage.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private ChatSession session;

    private String sender; // "user" or "AI"

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "jsonb")
    private String context;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
