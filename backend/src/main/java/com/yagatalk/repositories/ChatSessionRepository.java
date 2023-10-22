package com.yagatalk.repositories;

import com.yagatalk.domain.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class ChatSessionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ChatSession session) {
        jdbcTemplate.update("INSERT INTO chat_session(id, context_id, created_time) values (?, ?, ?)",
                session.getId(), session.getContextId(), Timestamp.from(session.getCreatedTime()));
    }

    public Stream<ChatSession> getAllSessionsByContextID(UUID contextId){
        return jdbcTemplate.queryForStream("SELECT * FROM chat_session WHERE context_id = ?",
                extractChatSession,contextId);
    }

    private static final RowMapper<ChatSession> extractChatSession =
            (rs, rn) -> new ChatSession(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("context_id")),
                    rs.getTimestamp("created_time").toInstant()
            );
}
