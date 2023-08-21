package com.yagatalk.domain;

import java.time.Instant;
import java.util.UUID;

public class ChatSession{
    private final UUID id;

    private final UUID contextId;

    private final Instant createdTime;
    public ChatSession(UUID id, UUID contextId, Instant createdTime) {
        this.id = id;
        this.contextId = contextId;
        this.createdTime = createdTime;
    }

    public UUID getId() {
        return id;
    }

    public UUID getContextId() {
        return contextId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

}

