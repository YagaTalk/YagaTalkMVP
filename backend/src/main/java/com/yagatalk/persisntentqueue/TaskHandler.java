package com.yagatalk.persisntentqueue;

public interface TaskHandler<T> {
    void execute(T task);
}
