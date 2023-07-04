package com.yagatalk.domain;

import java.util.UUID;

public class ChatSession{
    private UUID id;

    private UUID contextId;

    public ChatSession() {
    }

    public ChatSession(UUID id, UUID contextId) {
        this.id = id;
        this.contextId = contextId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getContextId() {
        return contextId;
    }

    public void setContextId(UUID contextId) {
        this.contextId = contextId;
    }
}

