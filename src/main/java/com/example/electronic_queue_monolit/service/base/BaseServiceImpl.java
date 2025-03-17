package com.example.electronic_queue_monolit.service.base;

import com.example.electronic_queue_monolit.domain.model.base.BaseEntity;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseServiceImpl<ENTITY extends BaseEntity, DTO, REPO extends BaseRepository<ENTITY>> implements BaseService<ENTITY, DTO> {

    protected final REPO repo;

    public BaseServiceImpl(REPO repo) {
        this.repo = repo;
    }

    @Override
    public DTO create(DTO dto) {
        ENTITY entity = toEntity(dto);
        return toDTO(repo.save(entity));
    }

    @Override
    public DTO findById(Long id) {
        return repo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Not found!"));
    }

    @Override
    public DTO update(Long id, DTO dto) {
        ENTITY entity = toEntity(dto);
        try {
            var idField = entity.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка установки ID", e);
        }
        return toDTO(repo.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<DTO> findAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<DTO> findAllWithPagination(Pageable pageable) {
        return repo.findAll(pageable).map(this::toDTO);
    }
}
