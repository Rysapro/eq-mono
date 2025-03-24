package com.example.electronic_queue_monolit.service;

import java.util.concurrent.TimeUnit;

public interface ScheduledTaskService {
    void scheduleTask(Runnable task, long delay, TimeUnit timeUnit);
} 