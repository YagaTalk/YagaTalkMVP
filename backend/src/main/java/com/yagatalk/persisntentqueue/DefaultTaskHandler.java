package com.yagatalk.persisntentqueue;

public abstract class DefaultTaskHandler<T> implements TaskHandler<T> {
    public abstract Class<T> getTaskClass();
}
