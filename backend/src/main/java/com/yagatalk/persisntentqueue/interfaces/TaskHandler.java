package com.yagatalk.persisntentqueue.interfaces;

public interface TaskHandler<T> {
    void execute(T task);
}
