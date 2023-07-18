package com.yagatalk.persisntentqueue;

import com.yagatalk.persisntentqueue.interfaces.ResponseTask;
import com.yagatalk.utill.UUIDGenerator;

import java.time.Instant;
import java.util.UUID;

public class TaskQueue {
    private final UUID id;
    private final TaskType taskType;
    private final ResponseTask task;
    private final State state;
    private final Instant submittedAt;

    public TaskQueue(UUID id, TaskType taskType, ResponseTask task, State state, Instant submittedAt) {
        this.id = id;
        this.taskType = taskType;
        this.task = task;
        this.state = state;
        this.submittedAt = submittedAt;
    }

    public TaskQueue(ResponseTask task) {
        this.id = UUIDGenerator.generateUUID();
        this.taskType = TaskType.GET_ASSISTANT_RESPONSE_TASK;
        this.task = task;
        this.state = State.PENDING;
        this.submittedAt = Instant.now();

    }

    public UUID getId() {
        return id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public ResponseTask getTask() {
        return task;
    }

    public State getState() {
        return state;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }
}



