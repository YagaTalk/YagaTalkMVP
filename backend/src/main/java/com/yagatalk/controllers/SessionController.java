package com.yagatalk.controllers;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.services.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<String> createChatSession(@RequestBody ChatSessionDTO chatSessionDto) {
       var id = chatSessionService.createChatSession(chatSessionDto.contextId());
        return ResponseEntity.status(201).body("{ \"id\":\""+ id +  "\"}");
    }

    private static record ChatSessionDTO(UUID contextId) {
    }

    @CrossOrigin
    @PostMapping("/{chatSessionId}/messages")
    public ResponseEntity<String> sendMessage(@PathVariable("chatSessionId") UUID chatSessionId,
                                                  @RequestBody MessageFromUserDTO message) {
        chatSessionService.createUserMessage(message.text(), chatSessionId);
        return ResponseEntity.status(201).body("{ ok }");
    }

    private record MessageFromUserDTO(String text) {
    }

    @CrossOrigin
    @Transactional
    @GetMapping("/{chatSessionId}/messages")
    public List<MessageDTO> getAllMessages(@PathVariable("chatSessionId") UUID chatSessionId,
                                           @RequestParam(value = "createdAfterMs", required = false) Optional<Long> ms) {
     return chatSessionService.getAllMessagesByChatSessionId(chatSessionId, ms)
                .filter(message -> !message.getRole().equals(Role.SYSTEM))
                .map(this::convertToMessageDTO)
                .toList();

    }

    private MessageDTO convertToMessageDTO(Message message) {
        return new MessageDTO(message.getRole(), message.getCreatedTime().toEpochMilli(), message.getContent());
    }

    private record MessageDTO(Role role, long created_at_ms, String content) {
    }
}
