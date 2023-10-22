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

    @PostMapping("/context")
    public ResponseEntity<String> createContext(@RequestBody ContextDTO contextDTO) {
        var id = chatSessionService.createContext(contextDTO.content,contextDTO.name);
        return ResponseEntity.status(201).body(new IdDTO(id).toString());
    }

    @GetMapping("/context/{contextId}")
    public Optional<ChatSessionService.ContextDTOWithContent> getContext(@PathVariable("contextId") UUID contextId){
        return chatSessionService.getContext(contextId);
    }

    private record ContextDTO(String content,String name){}

    @GetMapping("/contexts")
    public List<ChatSessionService.ContextDTO> getAllContexts(
            @RequestParam(value = "asc_sort", required = false) Optional<Boolean> ascSort,
            @RequestParam(name = "searchNameQuery", required = false) Optional<String> searchNameQuery,
            @RequestParam(name = "searchDateQuery", required = false) Optional<String> searchDateQuery) {
        return chatSessionService.getAllContexts(ascSort, searchNameQuery,searchDateQuery);
    }

    @GetMapping("/context/{contextId}/currentSession")
    public ResponseEntity<IdDTO> getCurrentChatSession(
            @PathVariable("contextId") UUID contextID) {
        var id = chatSessionService.createChatSession(contextID);
        return ResponseEntity.status(201).body(new IdDTO(id));
    }

    @GetMapping("/context/{contextId}/sessions")
    public List<ChatSessionService.ChatSessionDTO> getChatSession(@PathVariable("contextId") UUID contextID) {
        return chatSessionService.getAllChatSessionByContextID(contextID);
    }

    private record IdDTO(UUID id){
        @Override
        public String toString() {
            return "{" +
                    "\"id=\"" +id + "\"" +
                    '}';
        }
    }

    public record ChatSessionDTO(UUID contextId) {
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
