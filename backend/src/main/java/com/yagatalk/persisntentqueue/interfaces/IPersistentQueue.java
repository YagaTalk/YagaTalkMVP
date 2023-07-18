package com.yagatalk.persisntentqueue.interfaces;

public interface IPersistentQueue {
    <T> void registerHandler(Class<? extends T> taskType, TaskHandler<T> handler);

    void submit(Object task);
}