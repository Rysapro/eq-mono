package com.example.electronic_queue_monolit.domain.model;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "provision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provision extends BaseEntity {

    @Column(name = "provision_name", unique = true)
    private String provisionName;

    @Column(name = "code", unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

}
