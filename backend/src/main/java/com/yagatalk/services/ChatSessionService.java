package com.yagatalk.services;

import com.yagatalk.domain.Assistant;
import com.yagatalk.domain.ChatSession;
import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.persisntentqueue.IPersistentQueue;
import com.yagatalk.repositories.*;
import com.yagatalk.utill.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;

    private final IPersistentQueue persistentQueueImpl;

    private final AssistantRepository assistantRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository,
                              MessageRepository messageRepository,
                              IPersistentQueue persistentQueueImpl,
                              AssistantRepository assistantRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageRepository = messageRepository;
        this.persistentQueueImpl = persistentQueueImpl;
        this.assistantRepository = assistantRepository;

    }

    @Transactional
    public UUID createAssistant(boolean isTestSession, String content, String name, UUID assistantId) {
        Assistant.Status status = Assistant.Status.ACTIVE;
        if (isTestSession) {
            status = Assistant.Status.DRAFT;
        }
        var assistant = new Assistant(UUIDGenerator.generateUUID(), content, Instant.now(), name, assistantId, status, Instant.now());
        assistantRepository.save(assistant);
        return assistant.getId();
    }


    @Transactional
    public UUID createChatSession(UUID assistantId) {
        UUID id = UUID.randomUUID();
        var session = new ChatSession(id, assistantId, Instant.now());
        chatSessionRepository.save(session);

        createSystemMessage(assistantId, session.getId());
        return session.getId();
    }

    public List<ChatSessionDTO> getAllChatSessionsByAssistantId(UUID assistantID) {
        return chatSessionRepository.getAllSessionsByAssistantId(assistantID).map(this::convertToChatSessionDTO).toList();
    }

    public Optional<ChatSessionDTO> getLastChatSessionsByAssistantIdAndAuthorId(UUID assistantID, UUID authorId) {
        return chatSessionRepository.getLastSessionsByAssistantIdAndAuthorId(assistantID, authorId).map(this::convertToChatSessionDTO);
    }

    public List<ChatSessionDTO> getAllChatSessionsByAssistantIdAndAuthorId(UUID assistantID, UUID authorId) {
        return chatSessionRepository.getAllSessionsByAssistantIdAndAuthorId(assistantID, authorId).map(this::convertToChatSessionDTO).toList();
    }

    public record ChatSessionDTO(UUID id, Instant createdTime) {
    }

    private ChatSessionDTO convertToChatSessionDTO(ChatSession chatSession) {
        return new ChatSessionDTO(chatSession.getId(), chatSession.getCreatedTime());
    }

    @Transactional
    public void createSystemMessage(UUID assistantId, UUID sessionId) {
        String text = assistantRepository.getContent(assistantId);
        Message message = new Message(UUIDGenerator.generateUUID(),
                sessionId,
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

    public Optional<AssistantDTOWithContent> getAssistantById(UUID assistantId) {
        return assistantRepository.getById(assistantId).map(this::convertToAssistantDTOWithContent);
    }

    public Optional<AssistantDTOWithContent> getAssistantByAuthor(UUID assistantId, UUID authorId) {
        return assistantRepository.getByAuthor(assistantId, authorId).map(this::convertToAssistantDTOWithContent);
    }

    public record AssistantDTOWithContent(String content, Instant createdTime, String name, Assistant.Status status,
                                          Instant updatedTime) {
    }

    private AssistantDTOWithContent convertToAssistantDTOWithContent(Assistant assistant) {
        return new AssistantDTOWithContent(assistant.getContent(), assistant.getCreatedTime(), assistant.getName(), assistant.getStatus(), assistant.getUpdateTime());
    }

    public List<AssistantDTO> getAllAssistants(Optional<Boolean> ascSort,
                                               Optional<String> searchNameQuery,
                                               Optional<String> searchDateQuery) {
        if (ascSort.isPresent() && Boolean.TRUE.equals(ascSort.get())) {
            if (searchDateQuery.filter(StringUtils::isNotBlank).isPresent()) {
                return assistantRepository.getAllAssistantsWithSearchByDate("ASC", search(searchNameQuery), searchDateQuery.get()).map(this::convertToAssistantDTO).toList();
            }
            return assistantRepository.getAllAssistants("ASC", search(searchNameQuery)).map(this::convertToAssistantDTO).toList();
        }
        if (searchDateQuery.filter(StringUtils::isNotBlank).isPresent()) {
            return assistantRepository.getAllAssistantsWithSearchByDate("DESC", search(searchNameQuery), searchDateQuery.get()).map(this::convertToAssistantDTO).toList();
        }
        return assistantRepository.getAllAssistants("DESC", search(searchNameQuery)).map(this::convertToAssistantDTO).toList();
    }

    public List<AssistantDTO> getAllAuthorAssistants(Optional<Boolean> ascSort,
                                                     Optional<String> searchNameQuery,
                                                     Optional<String> searchDateQuery,
                                                     UUID authorId) {

        if (ascSort.isPresent() && Boolean.TRUE.equals(ascSort.get())) {
            if (searchDateQuery.filter(StringUtils::isNotBlank).isPresent()) {
                return assistantRepository.getAllAssistantsByAuthorIdWithSearchByDate("ASC", search(searchNameQuery), searchDateQuery.get(), authorId).map(this::convertToAssistantDTO).toList();
            }
            return assistantRepository.getAllAssistantsByAuthorId("ASC", search(searchNameQuery), authorId).map(this::convertToAssistantDTO).toList();
        }
        if (searchDateQuery.filter(StringUtils::isNotBlank).isPresent()) {
            return assistantRepository.getAllAssistantsByAuthorIdWithSearchByDate("DESC", search(searchNameQuery), searchDateQuery.get(), authorId).map(this::convertToAssistantDTO).toList();
        }
        return assistantRepository.getAllAssistantsByAuthorId("DESC", search(searchNameQuery), authorId).map(this::convertToAssistantDTO).toList();
    }

    private String search(Optional<String> searchQuery) {
        return searchQuery.orElse("");
    }

    public record AssistantDTO(UUID id, String name, Instant createdTime, Assistant.Status status) {
    }

    private AssistantDTO convertToAssistantDTO(Assistant assistant) {
        return new AssistantDTO(assistant.getId(), assistant.getName(), assistant.getCreatedTime(), assistant.getStatus());
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
