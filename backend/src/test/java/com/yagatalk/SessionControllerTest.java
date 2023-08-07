package com.yagatalk;


import com.github.tomakehurst.wiremock.WireMockServer;

import com.yagatalk.config.ContainersEnvironment;
import com.yagatalk.controllers.SessionController;
import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import com.yagatalk.repositories.ContextRepository;
import com.yagatalk.repositories.MessageRepository;
import com.yagatalk.services.ChatSessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wiremock.com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;


import static com.github.tomakehurst.wiremock.client.WireMock.*;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = YagaTalkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerTest  extends ContainersEnvironment {

    @Autowired
    private SessionController sessionController;

    @Autowired MessageRepository messageRepository;

    @Autowired ContextRepository contextRepository;

    public WireMockServer wireMockServer;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        configureFor("localhost",8081);
        configureWireMockForMyEndpoint();
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    void createChatSession(){
        SessionController.ChatSessionDTO chatSessionDTO =
                new SessionController.ChatSessionDTO(UUID.fromString("a1e7e851-505b-4b62-b4de-5a56d46ee843"));

        var responseEntity = sessionController.createChatSession(chatSessionDTO);
        Assertions.assertEquals("201 CREATED", responseEntity.getStatusCode().toString());
        Assertions.assertEquals("{\"id=\"38ec9db4-a797-4f9b-b756-17afa59605e7\"}", responseEntity.getBody());
    }
    
    @Test
    void sendMessage() throws InterruptedException {
        var responseEntity = sessionController.sendMessage(UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"),
                new SessionController.MessageFromUserDTO("Hello"));
        Assertions.assertEquals("201 CREATED", responseEntity.getStatusCode().toString());
        List<MessageDTO> expectedMessages = new ArrayList<>();
        expectedMessages.add(new MessageDTO(Role.SYSTEM,contextRepository.getContent(UUID.fromString("a1e7e851-505b-4b62-b4de-5a56d46ee843"))));
        expectedMessages.add(new MessageDTO(Role.USER,"Hello"));
        expectedMessages.add(new MessageDTO(Role.ASSISTANT,"Hi!"));

        Thread.sleep(3000);

        List<MessageDTO> actualMessages = messageRepository.getAllMessagesByChatSessionId(UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"))
                .map(this::convertToMessageDTO)
                .toList();

        Assertions.assertIterableEquals(expectedMessages,actualMessages);
    }

    private record MessageDTO(Role role,  String content) {
    }
    private MessageDTO convertToMessageDTO(Message message) {
        return new MessageDTO(message.getRole(),message.getContent());
    }

    @Test
    void getAllMessage() {
        var actualMessages = sessionController.getAllMessages(
                UUID.fromString("38ec9db4-a797-4f9b-b756-17afa59605e7"),
                Optional.empty())
                .stream()
                .map(this::convertToMessageDTO)
                .toList();

        List<MessageDTO> expectedMessages = new ArrayList<>();
        expectedMessages.add(new MessageDTO(Role.USER,"Hello"));
        expectedMessages.add(new MessageDTO(Role.ASSISTANT,"Hi!"));

        Assertions.assertIterableEquals(expectedMessages,actualMessages);
    }

    private MessageDTO convertToMessageDTO(ChatSessionService.MessageDTO message) {
        return new MessageDTO(message.role(),message.content());
    }

    private void configureWireMockForMyEndpoint() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/v1/chat/completions"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"12345\",\"object\":\"gpt-3.5-turbo\",\"created\":1691350344,\"model\":\"text-davinci-002\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"Hi!\"},\"finishReason\":\"stop\"}]}")));
    }
}

