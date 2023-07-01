package com.yagatalk.openaiclient;

import java.util.List;

public record OpenAiRequest(
        String model,
        List<OpenAiMessage> messages,
        int temperature) {}