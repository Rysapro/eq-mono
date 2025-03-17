package com.example.electronic_queue_monolit.config;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Counter {
    private LocalDate currentDate = LocalDate.now();
    private AtomicInteger queueNumber = new AtomicInteger(1);

    public synchronized int getNextNumber() {
        LocalDate today = LocalDate.now();

        if (!today.equals(currentDate)) {
            currentDate = today;
            queueNumber.set(1);
        }

        return queueNumber.getAndIncrement();
    }
}