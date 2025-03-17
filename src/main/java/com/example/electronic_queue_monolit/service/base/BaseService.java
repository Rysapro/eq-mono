package com.example.electronic_queue_monolit.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<ENTITY, DTO> {

    DTO create(DTO dto);

    DTO findById(Long id);

    DTO update(Long id, DTO dto);

    void deleteById(Long id);

    List<DTO> findAll();

    Page<DTO> findAllWithPagination(Pageable pageable);

    ENTITY toEntity(DTO dto);

    DTO toDTO(ENTITY entity);
}


