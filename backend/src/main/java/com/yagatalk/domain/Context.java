package com.yagatalk.domain;

import java.util.UUID;

public class Context {
    private UUID id;
    private String content;

    public Context() {
    }

    public Context(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

