package com.yagatalk.openaiclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenAiResponse(
        String id,
        String object,
        int created,
        String model,
        List<OpenAiChoice> choices) {
    public static record OpenAiChoice(
            int index,
            OpenAiMessage message,
            @JsonProperty("finish_reason") String finishReason) {
    }

    public static record Usage(
            int prompt_tokens,
            @JsonProperty("completion_tokens") int completionTokens,
            @JsonProperty("total_tokens") int totalTokens) {
    }
}
