package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.ProvisionDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.domain.model.Provision;
import com.example.electronic_queue_monolit.repository.ProvisionRepository;
import com.example.electronic_queue_monolit.service.ProvisionService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProvisionServiceImpl extends BaseServiceImpl<Provision, ProvisionDto, ProvisionRepository> implements ProvisionService {

    public ProvisionServiceImpl(ProvisionRepository repo) {
        super(repo);
    }

    @Override
    public Provision toEntity(ProvisionDto dto) {
        Provision provision = new Provision();
        provision.setCode(dto.getCode());
        provision.setProvisionName(dto.getProvisionName());
        provision.setPlace(createEntityIfNotNull(
                dto.getPlace() != null ? dto.getPlace().getId() : null, id ->  {
                    Place place = new Place();
                    place.setId(id);
                    return place;
                }
        ));

        return provision;
    }
    private <T> T createEntityIfNotNull(Long id, java.util.function.Function<Long, T> constructor) {
        return id != null ? constructor.apply(id) : null;}

    @Override
    public ProvisionDto toDTO(Provision entity) {
        ProvisionDto dto = new ProvisionDto();
        dto.setId(entity.getId());
        dto.setProvisionName(entity.getProvisionName());
        dto.setCode(entity.getCode());
        if (entity.getPlace() != null) {
            PlaceDto placeDto = new PlaceDto();
            placeDto.setId(entity.getPlace().getId());
            placeDto.setName(entity.getPlace().getName());
            dto.setPlace(placeDto);
        }
        return dto;
    }
}

