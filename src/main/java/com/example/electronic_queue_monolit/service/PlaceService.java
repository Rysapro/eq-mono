package com.example.electronic_queue_monolit.service;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.PlaceTicketCountDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.service.base.BaseService;

import java.util.List;

public interface PlaceService extends BaseService<Place, PlaceDto> {
    List<PlaceTicketCountDto> getPending();
}
