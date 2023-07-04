package com.yagatalk.repositories;

import com.yagatalk.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Message> getAllMessagesByChatSessionId(UUID chatSessionId) {
        return jdbcTemplate.query("SELECT * FROM message where chat_session_id=?",
                new Object[]{chatSessionId},
                new BeanPropertyRowMapper<>(Message.class));
    }

    public void save(Message message) {
        jdbcTemplate.update("INSERT INTO message values (?,?,?,?,?)",
                message.getId(), message.getChatSessionId(), message.getRole().toString(), message.getCreatedTime(), message.getContent());
    }

}