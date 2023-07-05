package com.yagatalk.services;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.OpenAiClient;
import com.yagatalk.openaiclient.OpenAiMessage;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.repositories.ChatSessionRepository;
import com.yagatalk.repositories.ContextRepository;
import com.yagatalk.repositories.MessageRepository;
import com.yagatalk.utill.UUIDGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;
    private final OpenAiClient openAiClient;

    private final ContextRepository contextRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, MessageRepository messageRepository, OpenAiClient openAiClient, ContextRepository contextRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageRepository = messageRepository;
        this.openAiClient = openAiClient;
        this.contextRepository = contextRepository;
    }

    public void createChatSession(UUID contextId) {

        chatSessionRepository.save(contextId);
        createSystemMessage(contextId);

    }

    public List<Message> getAllMessagesByChatSessionId(UUID chatSessionId, Optional<Long> ms) {
        if (ms.isEmpty()){
            return messageRepository.getAllMessagesByChatSessionId(chatSessionId);
        }
        return getAllMessagesByChatSessionIdAfterMs(chatSessionId,ms.get());
    }

    public List<Message> getAllMessagesByChatSessionIdAfterMs(UUID chatSessionId,long ms) {
        return messageRepository.getAllMessagesByChatSessionIdAfterMs(chatSessionId,ms);
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

        List<OpenAiMessage> messages = messageRepository.getAllMessagesByChatSessionId(chatSessionId)
                .stream()
                .map(this::convertToOpenAiMessage)
                .toList();

        String text = openAiClient.getAssistantResponse(messages).message().content();
        Message message = new Message(UUIDGenerator.generateUUID(),
                chatSessionId,
                Role.ASSISTANT,
                Instant.now(),
                text);
        messageRepository.save(message);
    }

    private OpenAiMessage convertToOpenAiMessage(Message message) {
        return new OpenAiMessage(message.getRole(), message.getContent());
    }
}
