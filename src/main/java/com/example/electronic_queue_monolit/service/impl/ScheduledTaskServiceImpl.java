package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.service.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final TaskScheduler taskScheduler;

    @Autowired
    public ScheduledTaskServiceImpl(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void scheduleTask(Runnable task, long delay, TimeUnit timeUnit) {
        Instant executionTime = Instant.now().plus(Duration.ofMillis(timeUnit.toMillis(delay)));
        taskScheduler.schedule(task, executionTime);
    }
} 