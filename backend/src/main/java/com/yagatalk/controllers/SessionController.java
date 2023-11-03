package com.yagatalk.controllers;

import com.yagatalk.services.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/chat/")
public class SessionController {
    private final ChatSessionService chatSessionService;

    public SessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping("/assistant")
    public ResponseEntity<String> createAssistant(@RequestBody AssistantDTO assistantDTO) {
        var id = chatSessionService.createAssistant(assistantDTO.content, assistantDTO.name);
        return ResponseEntity.status(201).body(new IdDTO(id).toString());
    }

    @GetMapping("/assistant/{assistantId}")
    public Optional<ChatSessionService.AssistantDTOWithContent> getAssistant(@PathVariable("assistantId") UUID assistantId){
        return chatSessionService.getAssistant(assistantId);
    }

    private record AssistantDTO(String content, String name){}

    @GetMapping("/assistants")
    public List<ChatSessionService.AssistantDTO> getAllAssistants(
            @RequestParam(value = "asc_sort", required = false) Optional<Boolean> ascSort,
            @RequestParam(name = "searchNameQuery", required = false) Optional<String> searchNameQuery,
            @RequestParam(name = "searchDateQuery", required = false) Optional<String> searchDateQuery) {
        return chatSessionService.getAllAssistants(ascSort, searchNameQuery,searchDateQuery);
    }

    @GetMapping("/assistant/{assistantId}/currentSession")
    public ResponseEntity<IdDTO> getCurrentChatSession(
            @PathVariable("assistantId") UUID assistantID) {
        var id = chatSessionService.createChatSession(assistantID);
        return ResponseEntity.status(201).body(new IdDTO(id));
    }

    @GetMapping("/assistant/{assistantId}/sessions")
    public List<ChatSessionService.ChatSessionDTO> getChatSession(@PathVariable("assistantId") UUID assistantID) {
        return chatSessionService.getAllChatSessionByAssistantID(assistantID);
    }

    private record IdDTO(UUID id){
        @Override
        public String toString() {
            return "{" +
                    "\"id=\"" +id + "\"" +
                    '}';
        }
    }

    public record ChatSessionDTO(UUID assistantId) {
    }


    @PostMapping("/sessions/{chatSessionId}/messages")
    public ResponseEntity<String> sendMessage(@PathVariable("chatSessionId") UUID chatSessionId,
                                                  @RequestBody MessageFromUserDTO message) {
        chatSessionService.createUserMessage(message.text(), chatSessionId);
        return ResponseEntity.status(201).body("{ ok }");
    }

    public record MessageFromUserDTO(String text) {
    }


    @GetMapping("/sessions/{chatSessionId}/messages")
    public List<ChatSessionService.MessageDTO> getAllMessages(@PathVariable("chatSessionId") UUID chatSessionId,
                                                              @RequestParam(value = "createdAfterMs", required = false) Optional<Long> ms) {
     return chatSessionService.getAllMessagesByChatSessionId(chatSessionId, ms);

    }
}
