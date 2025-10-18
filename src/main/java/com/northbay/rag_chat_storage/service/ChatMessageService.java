package com.northbay.rag_chat_storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.northbay.rag_chat_storage.models.ChatMessage;
import com.northbay.rag_chat_storage.models.ChatSession;
import com.northbay.rag_chat_storage.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	@Autowired
    private  ChatMessageRepository messageRepository;

	public ChatMessage addMessage(ChatSession session, String sender, String content, String context) {
        ChatMessage message = ChatMessage.builder()
                .session(session)
                .sender(sender)
                .content(content)
                .context(context)
                // createdAt will be automatically set from @Builder.Default in ChatMessage
                .build();

        return messageRepository.save(message);
    }

    public Page<ChatMessage> getMessages(ChatSession session, Pageable pageable) {
        return messageRepository.findBySession(session, pageable);
    }
}