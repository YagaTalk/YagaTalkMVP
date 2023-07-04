package com.yagatalk.controllers;

import com.yagatalk.domain.Message;
import com.yagatalk.dto.ChatSessionDTO;
import com.yagatalk.dto.MessageDTO;
import com.yagatalk.dto.MessageFromUserDTO;
import com.yagatalk.services.ChatSessionService;
import com.yagatalk.services.MessageService;
import com.yagatalk.utill.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat/sessions")
public class SessionController {
    private final ChatSessionService chatSessionService;
    private final MessageService messageService;

    public SessionController(ChatSessionService chatSessionService, MessageService messageService) {
        this.chatSessionService = chatSessionService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createChatSession(@RequestBody ChatSessionDTO chatSessionDto) {

        UUID chatSessionId = UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7");

        chatSessionService.createChatSession(chatSessionId, chatSessionDto.contextId());
        messageService.createSystemMessage(chatSessionDto.contextId(), chatSessionId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/{chatSessionId}/messages")
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("chatSessionId") UUID chatSessionId,
                                                  @RequestBody MessageFromUserDTO message) {
        messageService.createUserMessage(message.text(), chatSessionId);
        messageService.createAssistantMessage(chatSessionId);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{chatSessionId}/messages")
    public List<MessageDTO> getAllMessages(@PathVariable("chatSessionId") UUID chatSessionId) {
        return messageService.getAllMessagesByChatSessionId(chatSessionId)
                .stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO convertToMessageDTO(Message message) {
        return new MessageDTO(message.getRole(), message.getCreatedTime(), message.getContent());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
