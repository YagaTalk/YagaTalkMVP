package com.yagatalk.repositories;

import com.yagatalk.domain.Assistant;
import com.yagatalk.domain.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;
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
        jdbcTemplate.update("INSERT INTO chat_session(id, assistant_id, created_time) values (?, ?, ?)",
                session.getId(), session.getAssistantId(), Timestamp.from(session.getCreatedTime()));
    }

    public void delete(UUID assistantId) {
        jdbcTemplate.update("DELETE FROM chat_session WHERE assistant_id=?", assistantId);
    }

    public Stream<ChatSession> getAllSessionsByAssistantId(UUID assistantId) {
        return jdbcTemplate.queryForStream("SELECT * FROM chat_session WHERE assistant_id = ?",
                extractChatSession, assistantId);
    }

    public Stream<ChatSession> getAllSessionsByAssistantIdAndAuthorId(UUID assistantId, UUID authorId) {
        return jdbcTemplate.queryForStream("SELECT cs.* FROM chat_session cs JOIN assistant a ON cs.assistant_id = a.id" +
                        " WHERE cs.assistant_id =? AND a.author_id = ?",
                extractChatSession, assistantId, authorId);
    }

    public Optional<ChatSession> getLastSessionsByAssistantIdAndAuthorId(UUID assistantId, UUID authorId) {
        var chatSession = jdbcTemplate.query("SELECT cs.* FROM chat_session cs JOIN assistant a ON cs.assistant_id = a.id" +
                        " WHERE cs.assistant_id = ? AND a.author_id = ? ORDER BY cs.created_time DESC LIMIT 1;",
                extractChatSession, assistantId, authorId);

        return chatSession.isEmpty() ? Optional.empty() : Optional.of(chatSession.get(0));
    }

    private static final RowMapper<ChatSession> extractChatSession =
            (rs, rn) -> new ChatSession(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("assistant_id")),
                    rs.getTimestamp("created_time").toInstant()
            );
}
