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
        jdbcTemplate.update("INSERT INTO assistant values (?,?,?,?)",
                assistant.getId(), assistant.getContent(), Timestamp.from(assistant.getCreatedTime()), assistant.getName());
    }

    public Optional<Assistant> get(UUID assistantId){
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM ASSISTANT WHERE id = ?",
                extractAssistant,
                assistantId));
    }

    public String getContent(UUID assistantId) {
        return jdbcTemplate.queryForObject("SELECT content FROM ASSISTANT WHERE id =? ",
                (resultSet, rowNum) -> resultSet.getString("content"),
                assistantId);
    }

    public Stream<Assistant> getAllAssistants(String ASC, String searchNameQuery) {
        return jdbcTemplate.queryForStream("SELECT * FROM ASSISTANT WHERE name LIKE ? ORDER BY created_time "+ASC,
                extractAssistant,
                "%" + searchNameQuery + "%");
    }

    public Stream<Assistant> getAllAssistantsWithSearchByDate(String ASC, String searchNameQuery, String searchDateQuery) {
        return jdbcTemplate.queryForStream("SELECT * FROM ASSISTANT WHERE name LIKE ? " +
                        "AND created_time >= ?::timestamp\n" +
                        "AND created_time <  ?::timestamp ORDER BY created_time "+ASC,
                extractAssistant,
                "%" + searchNameQuery + "%",
                searchDateQuery +" 00:00:00",
                searchDateQuery +" 23:59:59.999999");
    }

    private static final RowMapper<Assistant> extractAssistant =
            (rs, rn) -> new Assistant(UUID.fromString(
                    rs.getString("id")),
                    rs.getString("content"),
                    rs.getTimestamp("created_time").toInstant(),
                    rs.getString("name")
            );
}
