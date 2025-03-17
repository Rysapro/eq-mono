package com.example.electronic_queue_monolit.repository.base;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository <E extends BaseEntity> extends JpaRepository<E, Long > {
    Page<E> findAll(Pageable pageable);
}
