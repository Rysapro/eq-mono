package com.example.electronic_queue_monolit.domain.model;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place extends BaseEntity {


    @Column(name = "place_name", unique = true)
    private String name;
    @Column(name = "address", unique = true)
    private String address;
    @Column(name ="code", unique = true)
    private String code;


}
