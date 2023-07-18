package com.yagatalk.persisntentqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagatalk.persisntentqueue.interfaces.ResponseTask;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class TaskQueueRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskQueueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(TaskQueue taskQueue) {

        jdbcTemplate.update("INSERT INTO task_queue values (?,?,?::jsonb,?,?)",
                taskQueue.getId(),
                taskQueue.getTaskType().toString(),
                taskToJson(taskQueue.getTask()),
                taskQueue.getState().toString(),
                Timestamp.from(taskQueue.getSubmittedAt()));
    }

    private String taskToJson(ResponseTask task) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(TaskQueue taskQueue) {
        jdbcTemplate.update("DELETE FROM task_queue WHERE id = ?", taskQueue.getId());
    }

    public Stream<TaskQueue> getAllTasks() {
        return jdbcTemplate.queryForStream("SELECT * FROM task_queue",
                extractTaskQueue);
    }

    public Stream<TaskQueue> getAllTasksByState(State state) {
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

    private static final RowMapper<TaskQueue> extractTaskQueue =
            (rs, rn) -> new TaskQueue(UUID.fromString(
                    rs.getString("id")),
                    TaskType.valueOf(rs.getString("task_type")),
                    jsonToTask(rs.getString("task")),
                    State.valueOf(rs.getString("state")),
                    rs.getTimestamp("submitted_at").toInstant()
            );

    private static ResponseTask jsonToTask(String taskJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(taskJson, GetAssistantResponseTask.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
