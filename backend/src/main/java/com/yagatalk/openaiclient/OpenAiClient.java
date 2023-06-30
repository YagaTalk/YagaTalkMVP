package com.yagatalk.openaiclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class OpenAiClient {
    private static WebClient webClient;
    private static final String MODEL = "gpt-3.5-turbo";
    private static final int TEMPERATURE = 0;

    @Autowired
    public OpenAiClient(WebClient.Builder builder, OpenAiConfig config) {

        webClient = builder
                .baseUrl(config.apiUrl())
                .defaultHeaders(headers -> headers.setBearerAuth(config.apiKey()))
                .build();
    }

    public AssistantCompletion getAssistantResponse(List<OpenAiMessage> chatHistory) {
        OpenAiRequest request = new OpenAiRequest(MODEL, chatHistory, TEMPERATURE);
        OpenAiResponse response = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .block();

        if (response == null || response.choices().isEmpty()) {
            throw new RuntimeException("Failed to get response from OpenAI: status=405, body=some error message from OpenAI");
        }
        return convertToAssistantCompletion(response);
    }

    private AssistantCompletion convertToAssistantCompletion(OpenAiResponse openAiResponse) {
        return new AssistantCompletion(openAiResponse.created(),
                openAiResponse.choices().get(0).message());
    }

}
