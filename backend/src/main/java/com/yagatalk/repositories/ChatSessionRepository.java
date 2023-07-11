package com.yagatalk.repositories;

import com.yagatalk.domain.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ChatSessionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ChatSession session) {
        jdbcTemplate.update("INSERT INTO chat_session values ('38ec9db4-a797-4f9b-b756-17afa59605e7',?)",
                session.getContextId());
    }
}
