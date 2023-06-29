package com.yagatalk.yagatalk.openaiclient.model;

import java.util.List;

public record OpenAiRequest(String model,List<Message> messages,Integer temperature) {}