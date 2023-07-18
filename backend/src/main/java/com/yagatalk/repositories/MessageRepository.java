package com.yagatalk.repositories;

import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Stream<Message> getAllMessagesByChatSessionId(UUID chatSessionId) {
        return jdbcTemplate.queryForStream("SELECT * FROM message where chat_session_id=? ORDER BY created_time",
                extractMessage,
                chatSessionId);
    }

    public Stream<Message> getAllMessagesByChatSessionIdAfterMs(UUID chatSessionId, long ms) {

        return jdbcTemplate.queryForStream("SELECT * FROM message WHERE chat_session_id = ? " +
                        "AND EXTRACT(EPOCH FROM created_time) * 1000 > ? " +
                        "ORDER BY created_time;",
                extractMessage,
                chatSessionId,ms);

    }

    public Message getLastMessage(UUID chatSessionId) {
        return jdbcTemplate.queryForObject("SELECT *" +
                        "FROM message " +
                        "WHERE chat_session_id = ? " +
                        "ORDER BY created_time DESC " +
                        "LIMIT 1;",
                extractMessage,
                chatSessionId);
    }

    public void save(Message message) {
        jdbcTemplate.update("INSERT INTO message values (?,?,?,?,?)",
                message.getId(),message.getChatSessionId(), message.getRole().toString(), Timestamp.from(message.getCreatedTime()), message.getContent());
    }

    private static final RowMapper<Message> extractMessage =
            (rs, rn) -> new Message(UUID.fromString(
                    rs.getString("id")),
                    UUID.fromString(rs.getString("chat_session_id")),
                    Role.valueOf(rs.getString("role")),
                    rs.getTimestamp("created_time").toInstant(),
                    rs.getString("content")
            );

}