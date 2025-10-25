package com.northbay.rag_chat_storage.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.northbay.rag_chat_storage.models.ChatSession;

@DataJpaTest 
class ChatSessionRepositoryTest {

    @Autowired
    private ChatSessionRepository sessionRepository;

    private ChatSession session1;
    private ChatSession session2;

    @BeforeEach
    void setUp() {
       
        session1 = new ChatSession();
        session1.setUserId("user123");
        session1.setName("Session A");

        session2 = new ChatSession();
        session2.setUserId("user456");
        session2.setName("Session B");

        sessionRepository.save(session1);
        sessionRepository.save(session2);
    }

    /**
     * Test: findByUserId() should return sessions for the given user
     */
    @Test
    void testFindByUserId_ReturnsCorrectSessions() {
        List<ChatSession> userSessions = sessionRepository.findByUserId("user123");

        assertThat(userSessions).hasSize(1);
        assertThat(userSessions.get(0).getUserId()).isEqualTo("user123");
        assertThat(userSessions.get(0).getName()).isEqualTo("Session A");
    }

    /**
     * Test: findByUserId() should return empty list if no sessions for user
     */
    @Test
    void testFindByUserId_NoSessionsFound() {
        List<ChatSession> userSessions = sessionRepository.findByUserId("unknown_user");

        assertThat(userSessions).isEmpty();
    }

    /**
     * Test: save and findById() basic CRUD
     */
    @Test
    void testSaveAndFindById_Success() {
        ChatSession newSession = new ChatSession();
        newSession.setUserId("user789");
        newSession.setName("Session C");

        ChatSession saved = sessionRepository.save(newSession);
        assertThat(saved.getId()).isNotNull();

        ChatSession fetched = sessionRepository.findById(saved.getId()).orElse(null);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getUserId()).isEqualTo("user789");
        assertThat(fetched.getName()).isEqualTo("Session C");
    }

    /**
     *  Test: deleteById() should remove session
     */
    @Test
    void testDeleteById_RemovesSession() {
        UUID id = session1.getId();

        sessionRepository.deleteById(id);

        boolean exists = sessionRepository.findById(id).isPresent();
        assertThat(exists).isFalse();
    }
}
