package com.yagatalk.repositories;

import com.yagatalk.domain.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public class ContextRepository {
    private final JdbcTemplate jdbcTemplate;

    public ContextRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Context context) {
        jdbcTemplate.update("INSERT INTO context values (?,?)",
                context.getId(),context.getContent());
    }

    public String getContent(UUID contextId) {

        return jdbcTemplate.queryForObject("SELECT content FROM context WHERE id =?",
                (resultSet, rowNum) -> resultSet.getString("content"),
                contextId);
    }
}
