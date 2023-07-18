package com.yagatalk.services;

import com.yagatalk.domain.ChatSession;
import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.persisntentqueue.GetAssistantResponseTask;
import com.yagatalk.persisntentqueue.interfaces.IPersistentQueue;
import com.yagatalk.repositories.*;
import com.yagatalk.utill.UUIDGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;

    private final IPersistentQueue myPersistentQueue;

    private final ContextRepository contextRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository,
                              MessageRepository messageRepository,
                              IPersistentQueue myPersistentQueue,
                              ContextRepository contextRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageRepository = messageRepository;
        this.myPersistentQueue = myPersistentQueue;
        this.contextRepository = contextRepository;

    }

    public UUID createChatSession(UUID contextId) {
        var session = new ChatSession(UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"), contextId);
        chatSessionRepository.save(session);
        createSystemMessage(contextId);
        return session.getId();
    }

    public Stream<Message> getAllMessagesByChatSessionId(UUID chatSessionId, Optional<Long> ms) {
        return ms
                .map(afterMs -> getAllMessagesByChatSessionIdAfterMs(chatSessionId, afterMs))
                .orElseGet(() -> messageRepository.getAllMessagesByChatSessionId(chatSessionId));
    }

    public Stream<Message> getAllMessagesByChatSessionIdAfterMs(UUID chatSessionId, long ms) {
        return messageRepository.getAllMessagesByChatSessionIdAfterMs(chatSessionId, ms);
    }

    public Message getLastMessageByChatSessionId(UUID chatSessionId) {
        return messageRepository.getLastMessage(chatSessionId);
    }

    public void createUserMessage(String text, UUID chatSessionId) {
        Message message = new Message(UUIDGenerator.generateUUID(),
                chatSessionId,
                Role.USER,
                Instant.now(),
                text);
        messageRepository.save(message);
        createAssistantMessage(chatSessionId);
    }

    public void createSystemMessage(UUID contextId) {
        String text = contextRepository.getContent(contextId);
        Message message = new Message(UUIDGenerator.generateUUID(),
                UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"),
                Role.SYSTEM,
                Instant.now(),
                text);
        messageRepository.save(message);
    }

    public void createAssistantMessage(UUID chatSessionId) {
        myPersistentQueue.submit(new GetAssistantResponseTask(chatSessionId));
    }
}
