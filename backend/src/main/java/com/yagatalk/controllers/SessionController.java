package com.yagatalk.controllers;

import com.yagatalk.domain.Assistant;
import com.yagatalk.services.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.yagatalk.utill.JwtUtil.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final ChatSessionService chatSessionService;

    public SessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }


    @GetMapping
    public ResponseEntity<?> getChatSessions(
            @AuthenticationPrincipal Jwt principal,
            @RequestParam(value = "assistantId") UUID assistantId) {
        if (hasRoleAdmin(principal)) {
            return ResponseEntity.status(200).body(chatSessionService.getAllChatSessionsByAssistantId(assistantId));

        }
        if (hasRoleAuthor(principal)) {
            UUID authorId = getUserId(principal);
            return ResponseEntity.status(200).body(chatSessionService.getAllChatSessionsByAssistantIdAndAuthorId(assistantId, authorId));
        }

        return ResponseEntity.status(401).body("Not authorized");
    }

    ///maybe rename into createCurrentChatSession
    //4.2 правда ли что всегда создается новая сессия
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentChatSession(
            @AuthenticationPrincipal Optional<Jwt> principal,
            @RequestParam(value = "assistantId") UUID assistantId) {
        var assistant = chatSessionService.getAssistantById(assistantId);

        if (assistant.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("assistant not found by id=" + assistantId);
        }

        if (principal == null) {
            var id = chatSessionService.createChatSession(assistantId);
            return ResponseEntity.status(201).body(new IdDTO(id));
        }

        if (assistant.get().status().equals(Assistant.Status.DRAFT)) {
            if (!hasRoleAdmin(principal.get()) && !hasRoleAuthor(principal.get())) {
                return ResponseEntity.status(403).body("access to DRAFT assistant is forbidden");
            }
            var id = chatSessionService.createChatSession(assistantId);
            return ResponseEntity.status(201).body(new IdDTO(id));
        }

        if (assistant.get().createdTime().equals(assistant.get().updatedTime())) {
            UUID authorId = getUserId(principal.get());
            var chatSessionDTO = chatSessionService.getLastChatSessionsByAssistantIdAndAuthorId(assistantId, authorId);
            if (chatSessionDTO.isPresent()) {
                return ResponseEntity.status(201).body(chatSessionDTO.get().id());
            }
        }

        var id = chatSessionService.createChatSession(assistantId);
        return ResponseEntity.status(201).body(new IdDTO(id));
    }

    private record IdDTO(UUID id) {
        @Override
        public String toString() {
            return "{" +
                    "\"id=\"" + id + "\"" +
                    '}';
        }
    }

    public record ChatSessionDTO(UUID assistantId) {
    }


    @PostMapping("/{chatSessionId}/messages")
    public ResponseEntity<String> sendMessage(@PathVariable("chatSessionId") UUID chatSessionId,
                                              @RequestBody MessageFromUserDTO message) {
        chatSessionService.createUserMessage(message.text(), chatSessionId);
        return ResponseEntity.status(201).body("{ ok }");
    }

    public record MessageFromUserDTO(String text) {
    }


    @GetMapping("/{chatSessionId}/messages")
    public List<ChatSessionService.MessageDTO> getAllMessages(@PathVariable("chatSessionId") UUID chatSessionId,
                                                              @RequestParam(value = "createdAfterMs", required = false) Optional<Long> ms) {
        return chatSessionService.getAllMessagesByChatSessionId(chatSessionId, ms);

    }
}
