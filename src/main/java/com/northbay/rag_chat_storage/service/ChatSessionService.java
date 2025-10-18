package com.northbay.rag_chat_storage.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.northbay.rag_chat_storage.exceptions.NotFoundException;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.repository.ChatSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatSessionService {

	@Autowired
    private  ChatSessionRepository sessionRepository;
	
	

    public ChatSession createSession(String userId, String name) {
    	ChatSession session = ChatSession.builder()
    		    .userId("userId123")
    		    .name("My Chat")
    		    .favorite(true)
    		    .build();
        return sessionRepository.save(session);
    }
    

    public ChatSession getSession(UUID id) {
        return sessionRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Session not found"));
    }

  

    public ChatSession renameSession(UUID id, String newName) {
        ChatSession session = getSession(id);
        session.setName(newName);
        return sessionRepository.save(session);
    }

    public ChatSession markFavorite(UUID id, boolean favorite) {
        ChatSession session = getSession(id);
        session.setFavorite(favorite);
        return sessionRepository.save(session);
    }

    public void deleteSession(UUID id) {
    	if (!sessionRepository.existsById(id)) {
            throw new NotFoundException("Session not found: " + id);
        }
        ChatSession session = getSession(id);
        sessionRepository.delete(session);
    }

	public List<ChatSession> getAllSessions(String userId) {
		
		return sessionRepository.findAll();
	}
}