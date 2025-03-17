package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.TicketStatusDto;
import com.example.electronic_queue_monolit.domain.model.TicketStatus;
import com.example.electronic_queue_monolit.repository.TicketStatusRepository;
import com.example.electronic_queue_monolit.service.TicketStatusService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class TicketStatusServiceImpl extends BaseServiceImpl<TicketStatus, TicketStatusDto, TicketStatusRepository> implements TicketStatusService {

    public TicketStatusServiceImpl(TicketStatusRepository repo) {
        super(repo);
    }

    @Override
    public TicketStatus toEntity(TicketStatusDto dto) {
        return new TicketStatus(dto.getName());
    }

    @Override
    public TicketStatusDto toDTO(TicketStatus entity) {
        TicketStatusDto dto = new TicketStatusDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
