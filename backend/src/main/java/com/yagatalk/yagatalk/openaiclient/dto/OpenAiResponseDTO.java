package com.yagatalk.yagatalk.openaiclient.dto;

import com.yagatalk.yagatalk.openaiclient.model.Message;

public record OpenAiResponseDTO (Integer created, Message message) {
}
