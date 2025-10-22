package com.northbay.rag_chat_storage.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;



import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id 
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private ChatSession session;

    private String sender; // "user" or "AI"

    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(columnDefinition = "text")
    private String context;


    private LocalDateTime createdAt;

    // Default no-args constructor required by JPA
    public ChatMessage() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for creating new messages
    public ChatMessage(ChatSession session, String sender, String content, String context) {
        this.session = session;
        this.sender = sender;
        this.content = content;
        this.context = context;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ChatSession getSession() { return session; }
    public void setSession(ChatSession session) { this.session = session; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

   
    public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
