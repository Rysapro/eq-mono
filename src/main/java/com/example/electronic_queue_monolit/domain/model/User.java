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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "password")
    private String password;

    @Column(name = "blocked")
    private Boolean blocked;

    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;
}
