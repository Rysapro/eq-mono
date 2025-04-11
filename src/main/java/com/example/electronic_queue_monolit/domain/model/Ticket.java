package com.example.electronic_queue_monolit.domain.model;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends BaseEntity {

    private Timestamp timeOfCreateTicket;

    @Column(name = "quantity")
    private String number;

    private LocalDateTime timeOfFinished;
    
    @Column(name = "processing_time")
    private Long processingTimeSeconds;

    @Transient
    private Duration processingTime;

    public Duration getProcessingTime() {
        return processingTimeSeconds != null ? Duration.ofSeconds(processingTimeSeconds) : null;
    }

    public void setProcessingTime(Duration duration) {
        this.processingTime = duration;
        this.processingTimeSeconds = duration != null ? duration.getSeconds() : null;
    }

    @ManyToOne
    @JoinColumn(name = "users")
    private User operatorId;

    @ManyToOne
    @JoinColumn(name = "provision")
    private Provision provision;

    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "info")
    private Information information;

    @ManyToOne
    @JoinColumn(name = "ticket_status")
    private TicketStatus ticketStatus;

    @ManyToOne
    @JoinColumn(name = "windows")
    private Window window;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }
}
