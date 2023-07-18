package com.yagatalk.persisntentqueue;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.OpenAiClient;
import com.yagatalk.openaiclient.OpenAiMessage;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.repositories.MessageRepository;
import com.yagatalk.utill.UUIDGenerator;
import com.yagatalk.persisntentqueue.interfaces.TaskHandler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service

public class GetAssistantResponseTaskHandler implements TaskHandler<GetAssistantResponseTask> {

    private final MessageRepository messageRepository;
    private final OpenAiClient openAiClient;


    public GetAssistantResponseTaskHandler(MessageRepository messageRepository, OpenAiClient openAiClient, TaskQueueRepository taskQueueRepository) {
        this.messageRepository = messageRepository;
        this.openAiClient = openAiClient;
    }

    @Override
    public void execute(GetAssistantResponseTask task) {
        if (!messageRepository.getLastMessage(task.chatSessionId()).getRole().equals(Role.ASSISTANT)) {
            List<OpenAiMessage> messages = messageRepository.getAllMessagesByChatSessionId(task.chatSessionId())
                    .map(this::convertToOpenAiMessage)
                    .toList();


            String text = openAiClient.getAssistantResponse(messages).message().content();
            Message message = new Message(UUIDGenerator.generateUUID(),
                    task.chatSessionId(),
                    Role.ASSISTANT,
                    Instant.now(),
                    text);
            if (!messageRepository.getLastMessage(task.chatSessionId()).getRole().equals(Role.ASSISTANT)) {
                messageRepository.save(message);
            }
        }
    }

    private OpenAiMessage convertToOpenAiMessage(Message message) {
        return new OpenAiMessage(message.getRole(), message.getContent());
    }
}
