package com.yagatalk.controllers;

import com.yagatalk.repositories.AssistantRepository;
import com.yagatalk.utill.InvalidRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
public class UiController {

    private final AssistantRepository assistantRepository;

    public UiController(AssistantRepository assistantRepository) {
        this.assistantRepository = assistantRepository;
    }

    @GetMapping("/")
    public byte[] getAdminUi() {
        return loadResource("pages/admin-console.html");
    }

    @GetMapping("/embeddable-chat/{assistantId}")
    public byte[] getEmbeddableChatComponent(@PathVariable("assistantId") UUID assistantId) {
        var assistant = assistantRepository.get(assistantId);
        return assistant
                .map(c -> loadResource("pages/embeddable-chat-page.html"))
                .orElseThrow(() -> new InvalidRequestException("assistant not found"));
    }

    private static byte[] loadResource(String path) {
        try(var html = UiController.class.getClassLoader().getResourceAsStream(path)) {
            if (html == null) {
                throw new IllegalStateException("file not found: " + path);
            }
            return html.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("failed to read file " + path, e);
        }
    }
}
