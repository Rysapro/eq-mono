package com.example.electronic_queue_monolit.domain.model;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

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

    private Timestamp timeOfFinished;
    private Timestamp processingTime;

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

    @Column(name = "ticket_code")
    private String ticketCode;

    @ManyToOne
    @JoinColumn(name = "ticket_status")
    private TicketStatus ticketStatus;


    @Column(name = "created_date")
    private LocalDate createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }
}
