package com.example.electronic_queue_monolit.domain.model;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "windows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Window extends BaseEntity {

    @Column(name = "quantity", unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "ticket")
    private Ticket processedTicket;

    @ManyToOne
    @JoinColumn(name = "users")
    private User operatorId;

    @ManyToOne
    @JoinColumn(name = "place")
    private Place place ;
}
