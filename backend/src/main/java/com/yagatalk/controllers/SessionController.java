package com.yagatalk.controllers;

import com.yagatalk.services.ChatSessionService;
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

    @PostMapping("/context/create")
    public ResponseEntity<String> createContext(@RequestBody ContextDTO contextDTO) {
        var id = chatSessionService.createContext(contextDTO.content);
        return ResponseEntity.status(201).body(new IdDTO(id).toString());
    }

    private record ContextDTO(String content){}

    @PostMapping
    public ResponseEntity<String> createChatSession(@RequestBody ChatSessionDTO chatSessionDto) {
       var id = chatSessionService.createChatSession(chatSessionDto.contextId());
        return ResponseEntity.status(201).body(new IdDTO(id).toString());
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
