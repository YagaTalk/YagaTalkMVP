package com.yagatalk.domain;

import java.util.UUID;

public class ChatSession{
    private final UUID id;

    private final UUID contextId;

    public ChatSession(UUID id, UUID contextId) {
        this.id = id;
        this.contextId = contextId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getContextId() {
        return contextId;
    }
}

