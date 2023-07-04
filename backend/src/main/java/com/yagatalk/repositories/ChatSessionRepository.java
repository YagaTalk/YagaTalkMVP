package com.yagatalk.repositories;

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

    public void save(UUID id, UUID contextId) {
        jdbcTemplate.update("INSERT INTO chat_session values (?,?)",
                id, contextId);
    }


}
