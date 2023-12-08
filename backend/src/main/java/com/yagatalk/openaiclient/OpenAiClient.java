package com.yagatalk.openaiclient;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

public class OpenAiClient {
    private static WebClient webClient;
    private static final String MODEL = "gpt-3.5-turbo-1106";
    private static final int TEMPERATURE = 0;

    public OpenAiClient(WebClient.Builder builder, OpenAiConfig config) {

        webClient = builder
                .baseUrl(config.apiUrl())
                .defaultHeaders(headers -> headers.setBearerAuth(config.apiKey()))
                .build();
    }

    public AssistantCompletion getAssistantResponse(List<OpenAiMessage> chatHistory) {
        OpenAiRequest request = new OpenAiRequest(MODEL, chatHistory, TEMPERATURE);
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .flatMap(this::toAssistantCompletion)
                .onErrorMap(this::toReadableError)
                .switchIfEmpty(Mono.error(new IllegalStateException("OpenAI request resulted with empty mono")))
                .block();
    }

    private Throwable toReadableError(Throwable e) {
        if (e instanceof WebClientResponseException er ){
            return new IllegalStateException("OpenAI request failed: status=" + er.getStatusCode() + " body=" + er.getResponseBodyAsString());
        }
        return new IllegalStateException("OpenAI request failed", e);
    }

    private Mono<AssistantCompletion> toAssistantCompletion(OpenAiResponse openAiResponse) {
        if (openAiResponse.choices().isEmpty()) {
            return Mono.error(new IllegalStateException("choices is empty"));
        }
        return Mono.just(new AssistantCompletion(openAiResponse.created(), openAiResponse.choices().get(0).message()));
    }

}
