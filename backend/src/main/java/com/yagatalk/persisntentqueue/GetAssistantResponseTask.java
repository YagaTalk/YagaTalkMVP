package com.yagatalk.persisntentqueue;

import com.yagatalk.persisntentqueue.interfaces.ResponseTask;

import java.util.UUID;

public record GetAssistantResponseTask(UUID chatSessionId) implements ResponseTask {

}
