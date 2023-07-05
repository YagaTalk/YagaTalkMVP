package com.yagatalk.repositories;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

        return jdbcTemplate.queryForStream("SELECT * FROM message where chat_session_id=?",
                new MessageRowMapper(),
                chatSessionId).toList();
    }

    public List<Message> getAllMessagesByChatSessionIdAfterMs(UUID chatSessionId,long ms) {
        return jdbcTemplate.queryForStream("SELECT * FROM message WHERE chat_session_id = ? AND EXTRACT(EPOCH FROM created_time) * 1000 > ?;",
                new MessageRowMapper(),
                chatSessionId,ms).toList();
    }

    public void save(Message message) {
        jdbcTemplate.update("INSERT INTO message values (?,?,?,?,?)",
                message.getId(),message.getChatSessionId(), message.getRole().toString(), Timestamp.from(message.getCreatedTime()), message.getContent());
    }

    static class MessageRowMapper implements RowMapper<Message> {

        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Message(UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("chat_session_id")),
                    Role.valueOf(rs.getString("role")),
                    rs.getTimestamp("created_time").toInstant(),
                    rs.getString("content"));
        }
    }

}