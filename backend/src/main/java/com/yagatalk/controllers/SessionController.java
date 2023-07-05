package com.yagatalk.controllers;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.services.ChatSessionService;
import com.yagatalk.utill.ErrorResponse;
import com.yagatalk.utill.InvalidRequestException;
import org.apache.catalina.connector.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat/sessions")
public class SessionController {
    private final ChatSessionService chatSessionService;

    public SessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createChatSession(@RequestBody ChatSessionDTO chatSessionDto) {
        chatSessionService.createChatSession(chatSessionDto.contextId());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    private static record ChatSessionDTO(UUID contextId) {
    }


    @PostMapping("/{chatSessionId}/messages")
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("chatSessionId") UUID chatSessionId,
                                                  @RequestBody MessageFromUserDTO message) {
        chatSessionService.createUserMessage(message.text(), chatSessionId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    private record MessageFromUserDTO(String text) {
    }


    @GetMapping("/{chatSessionId}/messages")
    public List<MessageDTO> getAllMessages(@PathVariable("chatSessionId") UUID chatSessionId,
                                           @RequestParam(value = "createdAfterMs", required = false) Optional<Long> ms,
                                           @RequestParam(value = "isUser",required = false)Optional <Boolean> isUser) {
        if (isUser.isEmpty()){
            return chatSessionService.getAllMessagesByChatSessionId(chatSessionId,ms)
                    .stream()
                    .map(this::convertToMessageDTO)
                    .toList();
        }
     return chatSessionService.getAllMessagesByChatSessionId(chatSessionId, ms)
                .stream()
                .filter(message -> !message.getRole().equals(Role.SYSTEM))
                .map(this::convertToMessageDTO)
                .toList();

    }

    private MessageDTO convertToMessageDTO(Message message) {
        return new MessageDTO(message.getRole(), message.getCreatedTime().toEpochMilli(), message.getContent());
    }

    private record MessageDTO(Role role, long created_at_ms, String content) {
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(InvalidRequestException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse("Internal server error");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
