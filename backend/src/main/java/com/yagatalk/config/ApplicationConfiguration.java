package com.yagatalk.config;

import com.yagatalk.openaiclient.OpenAiClient;
import com.yagatalk.openaiclient.OpenAiConfig;
import com.yagatalk.persisntentqueue.*;
import com.yagatalk.persisntentqueue.IPersistentQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
@EnableScheduling
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

    @Bean
    IPersistentQueue myPersistentQueue(PersistentQueueRepository persistentQueueRepository,
                                       @SuppressWarnings("rawtypes")
                                       List<DefaultTaskHandler> listOfHandlers) {
        PersistentQueueImpl myPersistentQueue = new PersistentQueueImpl(persistentQueueRepository);
        listOfHandlers.forEach(handler ->{
            myPersistentQueue.registerHandler(handler.getTaskClass(), handler); }


        );

        return myPersistentQueue;
    }

}
