package com.yagatalk.persisntentqueue;

public interface IPersistentQueue {
    <T> void registerHandler(Class<? extends T> taskType, DefaultTaskHandler<T> handler);

    void submit(Object task);
}