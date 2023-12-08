package com.yagatalk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public class Assistant {
    private final UUID id;
    private final String content;

    private final Instant createdTime;
    private final String name;

    private final UUID authorId;

    private final Status status;

    private Instant updateTime;

    public Assistant(UUID id, String content, Instant createdTime, String name, UUID authorId, Status status, Instant updateTime) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.content = content;
        this.authorId = authorId;
        this.status = status;
        this.updateTime = updateTime;
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

    public UUID getAuthorId() {
        return authorId;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public enum Status {
        @JsonProperty("draft")
        DRAFT,
        @JsonProperty("active")
        ACTIVE
    }
}


