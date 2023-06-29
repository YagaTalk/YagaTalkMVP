package com.yagatalk.yagatalk.openaiclient.model;

public record OpenAiResponse(String id,String object,Integer created,String model, OpenAiChoice[] choices) {
    public static record OpenAiChoice(Integer index, Message message, String finish_reason) {}
    public static record Usage(Integer prompt_tokens,Integer completion_tokens,Integer total_tokens) {}
}
