package com.yagatalk.controllers;

import com.yagatalk.services.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.yagatalk.utill.JwtUtil.*;


@RestController
@RequestMapping("/api/assistants")
public class AssistantController {

    private final ChatSessionService chatSessionService;

    public AssistantController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAssistants(@AuthenticationPrincipal Jwt principal,
                                              @RequestParam(value = "asc_sort", required = false) Optional<Boolean> ascSort,
                                              @RequestParam(name = "searchNameQuery", required = false) Optional<String> searchNameQuery,
                                              @RequestParam(name = "searchDateQuery", required = false) Optional<String> searchDateQuery) {
        if (hasRoleAdmin(principal)) {
            return ResponseEntity.status(200).body(chatSessionService.
                    getAllAssistants(ascSort, searchNameQuery, searchDateQuery));
        }

        if (hasRoleAuthor(principal)) {
            UUID authorId = getUserId(principal);
            return ResponseEntity.status(200).body(chatSessionService.
                    getAllAuthorAssistants(ascSort, searchNameQuery, searchDateQuery, authorId));
        }
        return ResponseEntity.status(401).body("Not authorized");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssistant(@AuthenticationPrincipal Jwt principal, @PathVariable("id") UUID assistantId) {
        if (hasRoleAdmin(principal)) {
            return ResponseEntity.status(200).body(chatSessionService.getAssistantById(assistantId));

        }
        if (hasRoleAuthor(principal)) {
            UUID authorId = getUserId(principal);
            return ResponseEntity.status(200).body(chatSessionService.getAssistantByAuthor(assistantId, authorId));
        }

        return ResponseEntity.status(401).body("Not authorized");
    }

    @PostMapping
    public ResponseEntity<String> createAssistant(@AuthenticationPrincipal Jwt principal, @RequestBody AssistantDTO assistantDTO) {
        var authorId = getUserId(principal);
        var id = chatSessionService.createAssistant(assistantDTO.content, assistantDTO.name, authorId);


        return ResponseEntity.status(201).body(new IdDTO(id).toString());
    }

    private record AssistantDTO(String content, String name) {
    }

    private record IdDTO(UUID id) {
        @Override
        public String toString() {
            return "{" +
                    "\"id=\"" + id + "\"" +
                    '}';
        }
    }
}
