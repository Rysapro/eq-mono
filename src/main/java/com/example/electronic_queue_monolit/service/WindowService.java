package com.example.electronic_queue_monolit.service;

import com.example.electronic_queue_monolit.domain.dto.WindowDto;
import com.example.electronic_queue_monolit.domain.model.Window;
import com.example.electronic_queue_monolit.service.base.BaseService;

import java.util.List;

public interface WindowService extends BaseService<Window, WindowDto> {
    List<WindowDto> findByPlaceId(Long placeId);

    List<WindowDto> getAll();
}
