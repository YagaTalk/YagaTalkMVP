package com.yagatalk.config;

import com.yagatalk.openaiclient.OpenAiClient;
import com.yagatalk.openaiclient.OpenAiConfig;
import com.yagatalk.persisntentqueue.*;
import com.yagatalk.persisntentqueue.interfaces.IPersistentQueue;
import com.yagatalk.persisntentqueue.interfaces.ResponseTask;
import com.yagatalk.persisntentqueue.interfaces.TaskHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
    IPersistentQueue myPersistentQueue(TaskQueueRepository taskQueueRepository,
                                       List<TaskHandler> listOfHandlers){
        MyPersistentQueue myPersistentQueue = new MyPersistentQueue(taskQueueRepository);
        listOfHandlers.forEach(handler -> {
            Type type = ((ParameterizedType) handler.getClass()
                    .getGenericInterfaces()[0])
                    .getActualTypeArguments()[0];
            if (type instanceof Class<?>) {
                Class<?> taskType = (Class<?>) type;
                if (GetAssistantResponseTask.class.isAssignableFrom(taskType)) {
                    myPersistentQueue.registerHandler(taskType, handler);
                }
            }
        });







//        MyPersistentQueue myPersistentQueue = new MyPersistentQueue(taskQueueRepository);
//        myPersistentQueue.registerHandler(GetAssistantResponseTask.class,getAssistantResponseTaskHandler);
        return myPersistentQueue;
    }

}
