package com.yagatalk.persisntentqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public
class PersistentQueueRepository {
    private final JdbcTemplate jdbcTemplate;

    public PersistentQueueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Task task) {

        jdbcTemplate.update("INSERT INTO task_queue values (?,?,?::jsonb,?,?)",
                task.getId(),
                task.getTaskType(),
                task.getTaskObject((taskType, payload) -> jsonToString(payload)),
                task.getState().toString(),
                Timestamp.from(task.getSubmittedAt()));
    }


    private String jsonToString(JsonNode task) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to convert json to String", e);
        }
    }

    public void delete(Task task) {
        jdbcTemplate.update("DELETE FROM task_queue WHERE id = ?", task.getId());
    }

    public Stream<Task> getAllTasks() {
        return jdbcTemplate.queryForStream("SELECT * FROM task_queue",
                extractTaskQueue);
    }

    public Stream<Task> getAllTasksByState(State state) {
        return jdbcTemplate.queryForStream("SELECT * FROM task_queue WHERE state = ?",
                extractTaskQueue,
                state.toString());
    }

    public void updateAllStateToPending() {
        jdbcTemplate.update("UPDATE task_queue set state = 'PENDING' WHERE state = 'RUNNING'");
    }

    public void updateState(UUID id, State state) {
        jdbcTemplate.update("UPDATE task_queue set state=? where id=?", state.toString(), id);
    }

    private static final RowMapper<Task> extractTaskQueue =
            (rs, rn) -> new Task(UUID.fromString(
                    rs.getString("id")),
                    rs.getString("task_type"),
                    stringToJson(rs.getString("task")),
                    State.valueOf(rs.getString("state")),
                    rs.getTimestamp("submitted_at").toInstant()
            );

    private static JsonNode stringToJson(String taskJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(taskJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to convert string to Json", e);
        }
    }
}
