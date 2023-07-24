package com.yagatalk.persisntentqueue;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagatalk.utill.UUIDGenerator;

import java.time.Instant;
import java.util.UUID;

class Task {
    private final UUID id;
    private final String taskType;
    private final JsonNode payload;
    private final State state;
    private final Instant submittedAt;

    public Task(UUID id, String taskType, JsonNode payload, State state, Instant submittedAt) {
        this.id = id;
        this.taskType = taskType;
        this.payload = payload;
        this.state = state;
        this.submittedAt = submittedAt;
    }

    public Task(String taskType, JsonNode payload) {
        this.id = UUIDGenerator.generateUUID();
        this.taskType = taskType;
        this.payload = payload;
        this.state = State.PENDING;
        this.submittedAt = Instant.now();

    }

    public UUID getId() {
        return id;
    }

    public String getTaskType() {
        return taskType;
    }

    public Object getTaskObject(ObjectDeserializer deserializer) {

        return deserializer.deserialize(this.taskType, this.payload);
    }

    @FunctionalInterface
    interface ObjectDeserializer {
        Object deserialize(String taskType, JsonNode payload);
    }

    public State getState() {
        return state;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }
}



