package com.northbay.rag_chat_storage.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;

@DataJpaTest // loads only JPA components + embedded database
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private ChatSessionRepository sessionRepository; 

    private ChatSession session;

    @BeforeEach
    void setUp() {
        // Create and save a test chat session
        session = new ChatSession();
        session.setName("Test Session");
        session = sessionRepository.save(session);
    }

    @Test
    void testFindBySession_ReturnsMessages() {
        // Given
        ChatMessage msg1 = new ChatMessage();
        msg1.setSession(session);
      
        msg1.setContent("Hello");

        ChatMessage msg2 = new ChatMessage();
        msg2.setSession(session);
       
        msg2.setContent("Hi there!");

        messageRepository.save(msg1);
        messageRepository.save(msg2);

        // When
        Page<ChatMessage> result = messageRepository.findBySession(session, PageRequest.of(0, 10));

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getSession().getId()).isEqualTo(session.getId());
    }

    @Test
    void testFindByIdAndSession_Found() {
        // Given
        ChatMessage msg = new ChatMessage();
        msg.setSession(session);
      
        msg.setContent("Hello AI");
        msg = messageRepository.save(msg);

        // When
        Optional<ChatMessage> result = messageRepository.findByIdAndSession(msg.getId(), session);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getContent()).isEqualTo("Hello AI");
    }

    @Test
    void testFindByIdAndSession_NotFoundWithDifferentSession() {
        // Given
        ChatMessage msg = new ChatMessage();
        msg.setSession(session);
      
        msg.setContent("Hi AI");
        msg = messageRepository.save(msg);

        // Create another session
        ChatSession anotherSession = new ChatSession();
        anotherSession.setName("Another Session");
        anotherSession = sessionRepository.save(anotherSession);

        // When
        Optional<ChatMessage> result = messageRepository.findByIdAndSession(msg.getId(), anotherSession);

        // Then
        assertThat(result).isEmpty();
    }
}
