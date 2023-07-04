package com.yagatalk.services;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.OpenAiClient;
import com.yagatalk.openaiclient.OpenAiMessage;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.repositories.ContextRepository;
import com.yagatalk.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final OpenAiClient openAiClient;

    private final ContextRepository contextRepository;

    public MessageService(MessageRepository messageRepository, OpenAiClient openAiClient, ContextRepository contextRepository) {
        this.messageRepository = messageRepository;
        this.openAiClient = openAiClient;
        this.contextRepository = contextRepository;
    }

    public List<Message> getAllMessagesByChatSessionId(UUID chatSessionId) {
        return messageRepository.getAllMessagesByChatSessionId(chatSessionId);
    }

    public void createUserMessage(String text, UUID chatSessionId) {
        Message message = new Message(UUID.randomUUID(),
                chatSessionId,
                Role.USER,
                LocalDateTime.now(),
                text);
        messageRepository.save(message);
    }

    public void createSystemMessage(UUID contextId, UUID chatSessionId) {
        String text = contextRepository.getContent(contextId);
        Message message = new Message(UUID.randomUUID(),
                chatSessionId,
                Role.SYSTEM,
                LocalDateTime.now(),
                text);
        messageRepository.save(message);
    }

    public void createAssistantMessage(UUID chatSessionId) {

        List<OpenAiMessage> messages = messageRepository.getAllMessagesByChatSessionId(chatSessionId)
                .stream()
                .map(this::convertToOpenAiMessage)
                .collect(Collectors.toList());

        String text = openAiClient.getAssistantResponse(messages).message().content();
        Message message = new Message(UUID.randomUUID(),
                chatSessionId,
                Role.ASSISTANT,
                LocalDateTime.now(),
                text);
        messageRepository.save(message);
    }

    private OpenAiMessage convertToOpenAiMessage(Message message) {
        return new OpenAiMessage(message.getRole(), message.getContent());
    }

}
