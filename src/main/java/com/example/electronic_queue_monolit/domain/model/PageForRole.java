package com.example.electronic_queue_monolit.domain.model;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "page")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageForRole extends BaseEntity {

    @Column(name = "name")
    private String name;

}
