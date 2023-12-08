package com.yagatalk.repositories;

import com.yagatalk.domain.Assistant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class AssistantRepository {
    private final JdbcTemplate jdbcTemplate;

    public AssistantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Assistant assistant) {
        jdbcTemplate.update("INSERT INTO assistant values (?,?,?,?,?,?,?)",
                assistant.getId(), assistant.getContent(), Timestamp.from(assistant.getCreatedTime()), assistant.getName(), assistant.getAuthorId(),
                assistant.getStatus().toString(), Timestamp.from(assistant.getUpdateTime()));
    }

    public Optional<Assistant> getById(UUID assistantId) {
        var assistant = jdbcTemplate.query("SELECT * FROM assistant WHERE id =? ", extractAssistant, assistantId);
        return assistant.isEmpty() ? Optional.empty() : Optional.of(assistant.get(0));
    }

    public Optional<Assistant> getByAuthor(UUID assistantId, UUID authorId) {
        var assistant = jdbcTemplate.query("SELECT * FROM assistant WHERE id =? and author_id=?", extractAssistant, assistantId, authorId);
        return assistant.isEmpty() ? Optional.empty() : Optional.of(assistant.get(0));
    }

    public String getContent(UUID assistantId) {
        return jdbcTemplate.queryForObject("SELECT content FROM ASSISTANT WHERE id =? ",
                (resultSet, rowNum) -> resultSet.getString("content"),
                assistantId);
    }

    public Stream<Assistant> getAllAssistants(String ASC, String searchNameQuery) {
        return jdbcTemplate.queryForStream("SELECT * FROM ASSISTANT WHERE name LIKE ? ORDER BY created_time " + ASC,
                extractAssistant,
                "%" + searchNameQuery + "%");
    }

    public Stream<Assistant> getAllAssistantsWithSearchByDate(String ASC, String searchNameQuery, String searchDateQuery) {
        return jdbcTemplate.queryForStream("SELECT * FROM ASSISTANT WHERE name LIKE ? " +
                        "AND created_time >= ?::timestamp\n" +
                        "AND created_time <  ?::timestamp ORDER BY created_time " + ASC,
                extractAssistant,
                "%" + searchNameQuery + "%",
                searchDateQuery + " 00:00:00",
                searchDateQuery + " 23:59:59.999999");
    }

    public Stream<Assistant> getAllAssistantsByAuthorId(String ASC, String searchNameQuery, UUID authorId) {
        return jdbcTemplate.queryForStream("SELECT * FROM ASSISTANT WHERE name LIKE ? AND author_id=? ORDER BY created_time " + ASC,
                extractAssistant,
                "%" + searchNameQuery + "%",
                authorId);
    }

    public Stream<Assistant> getAllAssistantsByAuthorIdWithSearchByDate(String ASC, String searchNameQuery, String searchDateQuery, UUID authorId) {
        return jdbcTemplate.queryForStream("SELECT * FROM ASSISTANT WHERE name LIKE ? AND author_id=? " +
                        "AND created_time >= ?::timestamp\n" +
                        "AND created_time <  ?::timestamp ORDER BY created_time " + ASC,
                extractAssistant,
                authorId,
                "%" + searchNameQuery + "%",
                searchDateQuery + " 00:00:00",
                searchDateQuery + " 23:59:59.999999");
    }

    private static final RowMapper<Assistant> extractAssistant =
            (rs, rn) -> new Assistant(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("content"),
                    rs.getTimestamp("created_time").toInstant(),
                    rs.getString("name"),
                    UUID.fromString(rs.getString("author_id")),
                    Assistant.Status.valueOf(rs.getString("status")),
                    rs.getTimestamp("updated_time").toInstant()
            );
}
