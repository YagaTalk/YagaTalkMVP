package com.yagatalk.domain;

import com.yagatalk.openaiclient.Role;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final UUID chatSessionId;
    private final Role role;
    private final Instant createdTime;
    private final String content;

    public Message(UUID id, UUID chatSessionId, Role role, Instant createdTime, String content) {
        this.id = id;
        this.chatSessionId = chatSessionId;
        this.role = role;
        this.createdTime = createdTime;
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public UUID getChatSessionId() {
        return chatSessionId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public Role getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}

