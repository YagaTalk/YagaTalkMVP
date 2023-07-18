package com.yagatalk.persisntentqueue;

import com.yagatalk.persisntentqueue.interfaces.IPersistentQueue;
import com.yagatalk.persisntentqueue.interfaces.ResponseTask;
import com.yagatalk.persisntentqueue.interfaces.TaskHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPersistentQueue implements IPersistentQueue {
    private final Map<Class<?>, TaskHandler<?>> handlerMap;
    private final TaskQueueRepository taskQueueRepository;

    public MyPersistentQueue(TaskQueueRepository taskQueueRepository) {
        this.taskQueueRepository = taskQueueRepository;
        handlerMap = new HashMap<>();
    }

    @Override
    public <T> void registerHandler(Class<? extends T> taskType, TaskHandler<T> handler) {
        if (handlerMap.containsKey(taskType)) {
            throw new IllegalArgumentException("Handler for task class already registered");
        }
        handlerMap.put(taskType, handler);
    }

    @Override
    public void submit(Object task) {
        Class<?> taskType = task.getClass();
        TaskHandler<Object> handler = (TaskHandler<Object>) handlerMap.get(taskType);
        if (handler == null) {
            throw new IllegalArgumentException("No handler for task class");
        }
        TaskQueue taskQueue = new TaskQueue((ResponseTask) task);
        taskQueueRepository.save(taskQueue);

    }

    @Scheduled(fixedDelay = 10000)
    public void processTasks() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            for (TaskQueue task : taskQueueRepository.getAllTasksByState(State.PENDING).toList()) {
                taskQueueRepository.updateState(task.getId(), State.RUNNING);
                Class<?> taskType = task.getTask().getClass();
                TaskHandler<Object> handler = (TaskHandler<Object>) handlerMap.get(taskType);
                handler.execute(task.getTask());
                taskQueueRepository.updateState(task.getId(), State.FINISHED);
            }
        });
        executor.shutdown();
    }

    @PostConstruct
    public void changePendingStateToRunning() {
        taskQueueRepository.updateAllStateToPending();
    }

}
