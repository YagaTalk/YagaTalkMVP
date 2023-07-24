package com.yagatalk;

import com.yagatalk.domain.ChatSession;
import com.yagatalk.repositories.ChatSessionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@SpringBootTest
@Testcontainers
class YagaTalkApplicationTests {
	private static final String DATABASE_NAME = "yagatalk-test";

	@Autowired
	private ChatSessionRepository chatSessionRepository;

	@Container
	public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
			.withReuse(true)
			.withDatabaseName(DATABASE_NAME);


	@BeforeAll
	static void start(){
		postgreSQLContainer.start();
	}
	@Test
	void contextLoads() {
		ChatSession chatSession = new ChatSession(UUID.randomUUID(),UUID.randomUUID());
		chatSessionRepository.save(chatSession);

	}

}
