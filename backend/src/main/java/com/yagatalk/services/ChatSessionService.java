package com.yagatalk.services;

import com.yagatalk.repositories.ChatSessionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository) {
        this.chatSessionRepository = chatSessionRepository;
    }

    public void createChatSession(UUID id, UUID contextId) {
        chatSessionRepository.save(id, contextId);
    }

}
