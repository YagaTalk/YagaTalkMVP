package com.yagatalk.controllers;

import com.yagatalk.services.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/assistants")
public class AssistantController {

    private final ChatSessionService chatSessionService;

    public AssistantController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @GetMapping()
    public List<ChatSessionService.AssistantDTO> getAllAssistants(Principal principal,
                                                                  @RequestParam(value = "asc_sort", required = false) Optional<Boolean> ascSort,
                                                                  @RequestParam(name = "searchNameQuery", required = false) Optional<String> searchNameQuery,
                                                                  @RequestParam(name = "searchDateQuery", required = false) Optional<String> searchDateQuery) {
        return chatSessionService.getAllAssistants(ascSort, searchNameQuery, searchDateQuery);
    }


    @GetMapping("/{id}")
    public Optional<ChatSessionService.AssistantDTOWithContent> getAssistant(Principal principal, @PathVariable("id") UUID assistantId) {

        return chatSessionService.getAssistant(assistantId);
    }
    

    @PostMapping()
    public ResponseEntity<String> createAssistant(Principal principal, @RequestBody AssistantDTO assistantDTO) {
        var id = chatSessionService.createAssistant(assistantDTO.content, assistantDTO.name);
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
