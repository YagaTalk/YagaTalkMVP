package com.yagatalk.domain;

import java.util.UUID;

public class Context {
    private final UUID id;
    private final String content;
    public Context(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

