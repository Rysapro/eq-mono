package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.dto.UserDto;
import com.example.electronic_queue_monolit.domain.dto.WindowDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.domain.model.Window;
import com.example.electronic_queue_monolit.repository.WindowRepository;
import com.example.electronic_queue_monolit.service.WindowService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WindowServiceImpl extends BaseServiceImpl<Window, WindowDto, WindowRepository> implements WindowService {

    public WindowServiceImpl(WindowRepository repo) {
        super(repo);
    }

    @Override
    public Window toEntity(WindowDto dto) {
        Window window = new Window();
        window.setNumber(dto.getNumber());
        window.setPlace(createEntityIfNotNull(
                dto.getPlace() != null ? dto.getPlace().getId() : null, id -> {
                    Place place = new Place();
                    place.setId(id);
                    return place;
                }
        ));
        window.setOperatorId(createEntityIfNotNull(
                dto.getOperator() != null ? dto.getOperator().getId() : null, id -> {
                    User user = new User();
                    user.setId(id);
                    return user;
                }
        ));

        return window;
    }
    private <T> T createEntityIfNotNull(Long id, java.util.function.Function<Long, T> constructor) {
        return id != null ? constructor.apply(id) : null;}

    @Override
    public WindowDto toDTO(Window entity) {
        WindowDto dto = new WindowDto();
        dto.setId(entity.getId());
        dto.setNumber(entity.getNumber());
        
        if (entity.getPlace() != null) {
            PlaceDto placeDto = new PlaceDto();
            placeDto.setId(entity.getPlace().getId());
            placeDto.setName(entity.getPlace().getName());
            dto.setPlace(placeDto);
        }
        
        if (entity.getProcessedTicket() != null) {
            TicketDto ticketDto = new TicketDto();
            ticketDto.setId(entity.getProcessedTicket().getId());
            ticketDto.setNumber(entity.getProcessedTicket().getNumber());
            dto.setProcessedTicket(ticketDto);
        }
        
        if (entity.getOperatorId() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(entity.getOperatorId().getId());
            userDto.setName(entity.getOperatorId().getName());
            dto.setOperator(userDto);
        }
        
        return dto;
    }

    @Override
    public WindowDto update(Long id, WindowDto windowDto) {
        Window existingWindow = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Window not found with id: " + id));

        // Update fields
        existingWindow.setNumber(windowDto.getNumber());

        existingWindow.setPlace(createEntityIfNotNull(
                windowDto.getPlace() != null ? windowDto.getPlace().getId() : null, placeId -> {
                    Place place = new Place();
                    place.setId(placeId);
                    return place;
                }
        ));

        existingWindow.setOperatorId(createEntityIfNotNull(
                windowDto.getOperator() != null ? windowDto.getOperator().getId() : null, userId -> {
                    User user = new User();
                    user.setId(userId);
                    return user;
                }
        ));

        return toDTO(repo.save(existingWindow));
    }
}
