package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PageDto;
import com.example.electronic_queue_monolit.domain.model.PageForRole;
import com.example.electronic_queue_monolit.repository.PageRepository;
import com.example.electronic_queue_monolit.service.PageService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl extends BaseServiceImpl<PageForRole, PageDto, PageRepository>
        implements PageService {

    public PageServiceImpl(PageRepository pageRepository) {
        super(pageRepository);
    }

    @Override
    public PageForRole toEntity(PageDto dto) {
        PageForRole page = new PageForRole();
        page.setName(dto.getName());
        return page;
    }

    @Override
    public PageDto toDTO(PageForRole entity) {
        PageDto dto = new PageDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
