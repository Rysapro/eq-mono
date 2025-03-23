package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PageDto;
import com.example.electronic_queue_monolit.domain.dto.RoleDto;
import com.example.electronic_queue_monolit.domain.model.PageForRole;
import com.example.electronic_queue_monolit.domain.model.Role;
import com.example.electronic_queue_monolit.repository.PageRepository;
import com.example.electronic_queue_monolit.repository.RoleRepository;
import com.example.electronic_queue_monolit.service.RoleService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleDto, RoleRepository> implements RoleService {

    private final PageRepository pageRepository;

    public RoleServiceImpl(RoleRepository repo, PageRepository pageRepository) {
        super(repo);
        this.pageRepository = pageRepository;
    }

    @Override
    public Role toEntity(RoleDto dto) {
        Role role = new Role();
        role.setName(dto.getName());
        
        List<PageForRole> pages = new ArrayList<>();
        
        if (dto.getPageId() != null) {
            pageRepository.findById(dto.getPageId()).ifPresent(pages::add);
        }
        else if (dto.getPage() != null && dto.getPage().getId() != null) {
            PageForRole page = new PageForRole();
            page.setId(dto.getPage().getId());
            pages.add(page);
        }
        
        role.setPage(pages);
        return role;
    }

    private <T> T createEntityIfNotNull(Long id, java.util.function.Function<Long, T> constructor) {
        return id != null ? constructor.apply(id) : null;
    }

    @Override
    public RoleDto toDTO(Role entity) {
        RoleDto dto = new RoleDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        
        if (!entity.getPage().isEmpty()) {
            PageForRole pageEntity = entity.getPage().get(0);
            
            dto.setPageId(pageEntity.getId());
            
            PageDto pageDto = new PageDto();
            pageDto.setId(pageEntity.getId());
            pageDto.setName(pageEntity.getName());
            dto.setPage(pageDto);
        }
        
        return dto;
    }
}