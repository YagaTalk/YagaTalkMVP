package com.yagatalk.services;

import com.yagatalk.domain.ChatSession;
import com.yagatalk.domain.Context;
import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.persisntentqueue.IPersistentQueue;
import com.yagatalk.repositories.*;
import com.yagatalk.utill.UUIDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;

    private final IPersistentQueue persistentQueueImpl;

    private final ContextRepository contextRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository,
                              MessageRepository messageRepository,
                              IPersistentQueue persistentQueueImpl,
                              ContextRepository contextRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageRepository = messageRepository;
        this.persistentQueueImpl = persistentQueueImpl;
        this.contextRepository = contextRepository;

    }

    @Transactional
    public UUID createContext(String content, String name) {
        var context = new Context(UUIDGenerator.generateUUID(), content, Instant.now(), name);
        contextRepository.save(context);
        return context.getId();
    }


    @Transactional
    public UUID createChatSession(UUID contextId) {
        var session = new ChatSession(UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"), contextId);
        chatSessionRepository.save(session);

        createSystemMessage(contextId);
        return session.getId();
    }

    @Transactional
    public void createSystemMessage(UUID contextId) {
        String text = contextRepository.getContent(contextId);
        Message message = new Message(UUIDGenerator.generateUUID(),
                UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"),
                Role.SYSTEM,
                Instant.now(),
                text);
        messageRepository.save(message);
    }

    @Transactional
    public List<MessageDTO> getAllMessagesByChatSessionId(UUID chatSessionId, Optional<Long> ms) {
        return ms
                .map(afterMs -> getAllMessagesByChatSessionIdAfterMs(chatSessionId, afterMs))
                .orElseGet(() -> messageRepository.getAllMessagesByChatSessionId(chatSessionId))
                .filter(message -> !message.getRole().equals(Role.SYSTEM))
                .map(this::convertToMessageDTO)
                .toList();
    }

    private MessageDTO convertToMessageDTO(Message message) {
        return new MessageDTO(message.getRole(), message.getCreatedTime().toEpochMilli(), message.getContent());
    }

    public ContextDTOWithContent getContext(UUID contextId){
        return convertToContextDTOWithContent(contextRepository.get(contextId));
    }

    public record ContextDTOWithContent(String content,Instant createdTime,String name){}

    private ContextDTOWithContent convertToContextDTOWithContent(Context context) {
        return new ContextDTOWithContent(context.getContent(),context.getCreatedTime(),context.getName());
    }

    public List<ContextDTO> getAllContexts(Optional<Boolean> ascSort,
                                           Optional<String> searchNameQuery,
                                           Optional<String> searchDateQuery) {
        if (ascSort.isPresent() && Boolean.TRUE.equals(ascSort.get())){
            if (searchDateQuery.isPresent() && !searchDateQuery.get().equals("")){
                return contextRepository.getAllContextsWithSearchByDate("ASC",search(searchNameQuery),searchDateQuery.get()).map(this::convertToContextDTO).toList();
            }
            return contextRepository.getAllContexts("ASC",search(searchNameQuery)).map(this::convertToContextDTO).toList();
        }
        if (searchDateQuery.isPresent() && !searchDateQuery.get().equals("")){
            return contextRepository.getAllContextsWithSearchByDate("DESC",search(searchNameQuery),searchDateQuery.get()).map(this::convertToContextDTO).toList();
        }
        return contextRepository.getAllContexts("DESC",search(searchNameQuery)).map(this::convertToContextDTO).toList();
    }
    private String search(Optional<String> searchQuery){
        return searchQuery.orElse("");
    }

    public record ContextDTO(UUID id,String name, Instant createdTime) {
    }

    private ContextDTO convertToContextDTO(Context context) {
        return new ContextDTO(context.getId(), context.getName(), context.getCreatedTime());
    }

    public record MessageDTO(Role role, long created_at_ms, String content) {
    }

    public Stream<Message> getAllMessagesByChatSessionIdAfterMs(UUID chatSessionId, long ms) {
        return messageRepository.getAllMessagesByChatSessionIdAfterMs(chatSessionId, ms);
    }


    public Message getLastMessageByChatSessionId(UUID chatSessionId) {
        return messageRepository.getLastMessage(chatSessionId);
    }

    @Transactional
    public void createUserMessage(String text, UUID chatSessionId) {
        Message message = new Message(UUIDGenerator.generateUUID(),
                chatSessionId,
                Role.USER,
                Instant.now(),
                text);
        messageRepository.save(message);
        persistentQueueImpl.submit(new GetAssistantResponseTask(chatSessionId));
    }

    @Transactional
    public void createAssistantMessage(UUID chatSessionId) {
        persistentQueueImpl.submit(new GetAssistantResponseTask(chatSessionId));
    }
}
