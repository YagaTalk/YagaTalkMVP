package com.yagatalk.yagatalk.openaiclient.service;

import com.yagatalk.yagatalk.openaiclient.model.Message;
import com.yagatalk.yagatalk.openaiclient.model.OpenAiRequest;
import com.yagatalk.yagatalk.openaiclient.dto.OpenAiResponseDTO;
import com.yagatalk.yagatalk.openaiclient.model.OpenAiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OpenAiService {
    private final WebClient webClient;
    private final String model = "gpt-3.5-turbo";
    private final Integer temperature = 0;

    @Autowired
    public OpenAiService(@Value("${openai.apiKey}") String apiKey,
                         @Value("${openai.apiUrl}") String apiUrl) {

        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();
    }

    public OpenAiResponseDTO getAssistantResponse(List<Message> chatHistory) {
        OpenAiRequest request = new OpenAiRequest(model,chatHistory,temperature);
        OpenAiResponse response = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .block();


        if (response != null) {
            return convertToOpenAiResponseDTO(response);
        } else {
            throw new RuntimeException("Failed to get response from OpenAI.");
        }
    }

    private  OpenAiResponseDTO convertToOpenAiResponseDTO(OpenAiResponse openAiResponse) {
        return new OpenAiResponseDTO(openAiResponse.created(),
                openAiResponse.choices()[0].message());
    }

}
