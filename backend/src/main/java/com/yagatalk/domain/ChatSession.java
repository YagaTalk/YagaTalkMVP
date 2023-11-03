package com.yagatalk.domain;

import java.time.Instant;
import java.util.UUID;

public class ChatSession{
    private final UUID id;

    private final UUID assistantId;

    private final Instant createdTime;
    public ChatSession(UUID id, UUID assistantId, Instant createdTime) {
        this.id = id;
        this.assistantId = assistantId;
        this.createdTime = createdTime;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAssistantId() {
        return assistantId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

}

