package com.yagatalk.domain;

import java.time.Instant;
import java.util.UUID;

public class Assistant {
    private final UUID id;
    private final String content;

    private final Instant createdTime;
    private final String name;

    public Assistant(UUID id, String content, Instant createdTime, String name) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }
}

