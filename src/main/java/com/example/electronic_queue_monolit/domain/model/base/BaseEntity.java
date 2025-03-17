package com.example.electronic_queue_monolit.domain.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
    @Column(name = "update_date")
    private LocalDateTime updateDate = LocalDateTime.now();

    @Transient
    protected String getTableName(BaseEntity entity) {
        Class<?> entityClass = entity.getClass();
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            return table.name();
        } else if (entityClass.isAnnotationPresent(Entity.class)) {
            return entityClass.getSimpleName();
        }
        throw new IllegalArgumentException("Class " + entityClass.getName() + " is not a valid JPA entity.");
    }
}

