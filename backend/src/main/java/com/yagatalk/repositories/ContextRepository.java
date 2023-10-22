package com.yagatalk.repositories;

import com.yagatalk.domain.Context;
import com.yagatalk.domain.Message;
import com.yagatalk.openaiclient.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class ContextRepository {
    private final JdbcTemplate jdbcTemplate;

    public ContextRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Context context) {
        jdbcTemplate.update("INSERT INTO context values (?,?,?,?)",
                context.getId(),context.getContent(), Timestamp.from(context.getCreatedTime()),context.getName());
    }

    public Context get(UUID contextId){
        return jdbcTemplate.queryForObject("SELECT * FROM CONTEXT WHERE id = ?",
                extractContext,
                contextId);
    }

    public String getContent(UUID contextId) {
        return jdbcTemplate.queryForObject("SELECT content FROM context WHERE id =? ",
                (resultSet, rowNum) -> resultSet.getString("content"),
                contextId);
    }

    public Stream<Context> getAllContexts(String ASC,String searchNameQuery) {
        return jdbcTemplate.queryForStream("SELECT * FROM context WHERE name LIKE ? ORDER BY created_time "+ASC,
                extractContext,
                "%" + searchNameQuery + "%");
    }

    public Stream<Context> getAllContextsWithSearchByDate(String ASC,String searchNameQuery,String searchDateQuery) {
        return jdbcTemplate.queryForStream("SELECT * FROM context WHERE name LIKE ? " +
                        "AND created_time >= ?::timestamp\n" +
                        "AND created_time <  ?::timestamp ORDER BY created_time "+ASC,
                extractContext,
                "%" + searchNameQuery + "%",
                searchDateQuery +" 00:00:00",
                searchDateQuery +" 23:59:59.999999");
    }

    private static final RowMapper<Context> extractContext =
            (rs, rn) -> new Context(UUID.fromString(
                    rs.getString("id")),
                    rs.getString("content"),
                    rs.getTimestamp("created_time").toInstant(),
                    rs.getString("name")
            );
}
