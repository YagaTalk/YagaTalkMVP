package com.yagatalk.config;

import com.yagatalk.openaiclient.OpenAiClient;
import com.yagatalk.openaiclient.OpenAiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfiguration {

    @Bean
    OpenAiConfig openAiConfig(@Value("${openai.apiKey}") String apiKey,
                              @Value("${openai.apiUrl}") String apiUrl) {
        return new OpenAiConfig(apiKey, apiUrl);
    }

    @Bean
    OpenAiClient openAiClient(WebClient.Builder builder, OpenAiConfig config) {
        return new OpenAiClient(builder, config);
    }
}
