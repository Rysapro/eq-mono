package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.InformationDto;
import com.example.electronic_queue_monolit.domain.model.Information;
import com.example.electronic_queue_monolit.repository.InformationRepository;
import com.example.electronic_queue_monolit.service.InformationService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class InformationServiceImpl extends BaseServiceImpl<Information, InformationDto, InformationRepository>
        implements InformationService {

    public InformationServiceImpl(InformationRepository repo) {
        super(repo);
    }

    @Override
    public Information toEntity(InformationDto dto) {
        Information information = new Information();
        information.setDeclaration(dto.getDeclaration());
        return information;
    }

    @Override
    public InformationDto toDTO(Information entity) {
        InformationDto dto = new InformationDto();
        dto.setId(entity.getId());
        dto.setDeclaration(entity.getDeclaration());
        return dto;
    }
}
