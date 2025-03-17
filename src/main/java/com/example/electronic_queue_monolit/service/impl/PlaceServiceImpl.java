package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.PlaceTicketCountDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.repository.PlaceRepository;
import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl extends BaseServiceImpl<Place, PlaceDto, PlaceRepository> implements PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository repo, PlaceRepository placeRepository) {
        super(repo);
        this.placeRepository = placeRepository;
    }

    @Override
    public Place toEntity(PlaceDto dto) {
        Place place = new Place();
        place.setName(dto.getName());
        place.setAddress(dto.getAddress());
        place.setCode(dto.getCode());
        return place;
    }

    @Override
    public PlaceDto toDTO(Place entity) {
        PlaceDto dto = new PlaceDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setCode(entity.getCode());
        return dto;
    }

    public List<PlaceTicketCountDto> getPending(){
        return placeRepository.countActiveTicketsPerPlace();
    }
}
