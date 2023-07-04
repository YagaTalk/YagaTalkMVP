package com.yagatalk.domain;

import com.yagatalk.openaiclient.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private UUID id;
    private UUID chatSessionId;
    private Role role;

    private LocalDateTime createdTime;
    private String content;

    public Message() {

    }

    public Message(UUID id, UUID chatSessionId, Role role, LocalDateTime createdTime, String content) {
        this.id = id;
        this.chatSessionId = chatSessionId;
        this.role = role;
        this.createdTime = createdTime;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(UUID chatSessionId) {
        this.chatSessionId = chatSessionId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

