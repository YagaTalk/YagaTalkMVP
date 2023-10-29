package com.yagatalk.persisntentqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

public class PersistentQueueImpl implements IPersistentQueue {

    private static final Logger logger = LoggerFactory.getLogger(PersistentQueueImpl.class);

    private final Map<Class<?>, DefaultTaskHandler<?>> handlerMap;
    private final PersistentQueueRepository persistentQueueRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int TREE_SECONDS = 3000;

    public PersistentQueueImpl(PersistentQueueRepository persistentQueueRepository) {
        this.persistentQueueRepository = persistentQueueRepository;
        handlerMap = new HashMap<>();
    }

    @Override
    public <T> void registerHandler(Class<? extends T> taskType, DefaultTaskHandler<T> handler) {
        if (handlerMap.containsKey(taskType)) {
            throw new IllegalArgumentException("Handler for task class already registered");
        }
        handlerMap.put(taskType, handler);
    }

    @Override
    public void submit(Object task) {
        Class<?> taskType = task.getClass();
        DefaultTaskHandler<Object> handler = (DefaultTaskHandler<Object>) handlerMap.get(taskType);
        if (handler == null) {
            throw new IllegalArgumentException("No handler for task class");
        }
        Task taskQueue = new Task(taskType.getName(),objectMapper.valueToTree(task));
        persistentQueueRepository.save(taskQueue);
    }

    @Scheduled(fixedDelay = TREE_SECONDS)
    @Transactional
    public void processTasksScheduler() {
        try {
            processTasks();
        } catch (Exception e) {
            logger.error("error occurred in async task", e);
        }
    }

    public void processTasks() {
        var tasks = persistentQueueRepository.getAllTasksByState(State.PENDING).toList();
        logger.info("found {} tasks to execute", tasks.size());

        for (Task task : tasks) {
            persistentQueueRepository.updateState(task.getId(), State.RUNNING);
            Object taskObject = task.getTaskObject((taskType, payload) -> jsonToTask(task.getId().toString(),
                    taskType,
                    payload));
            DefaultTaskHandler<Object> handler = (DefaultTaskHandler<Object>) handlerMap.get(taskObject.getClass());
            handler.execute(taskObject);
            persistentQueueRepository.updateState(task.getId(), State.FINISHED);
        }

        logger.info("completed {} tasks", tasks.size());
    }

    private static Object jsonToTask(String taskId,String taskType, JsonNode taskJson) {
        Class<?> taskClass = taskClassFromName(taskType);
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(taskJson), taskClass);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("failed to deserialize task id=" + taskId, e);
        }
    }

    private static Class<?> taskClassFromName(String taskType) {
        try {
            return Class.forName(taskType);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("unknown task type " + taskType);
        }
    }

    @PostConstruct
    public void changePendingStateToRunning() {
        persistentQueueRepository.updateAllStateToPending();
    }

}
