package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.dto.UserDto;
import com.example.electronic_queue_monolit.domain.dto.WindowDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.domain.model.Window;
import com.example.electronic_queue_monolit.repository.PlaceRepository;
import com.example.electronic_queue_monolit.repository.UserRepository;
import com.example.electronic_queue_monolit.repository.WindowRepository;
import com.example.electronic_queue_monolit.service.WindowService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WindowServiceImpl extends BaseServiceImpl<Window, WindowDto, WindowRepository> implements WindowService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public WindowServiceImpl(WindowRepository repo, PlaceRepository placeRepository, UserRepository userRepository) {
        super(repo);
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
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
        existingWindow.setNumber(windowDto.getNumber());
        existingWindow.setPlace(createEntityIfNotNull(
                windowDto.getPlace() != null ? windowDto.getPlace().getId() : null, placeId -> {
                    return placeRepository.findById(placeId)
                            .orElseThrow(() -> new RuntimeException("Place not found with id: " + placeId));
                }));
        existingWindow.setOperatorId(createEntityIfNotNull(
                windowDto.getOperator() != null ? windowDto.getOperator().getId() : null, userId -> {
                    return userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                }));
        
        return toDTO(repo.save(existingWindow));
    }
    
    @Override
    public List<WindowDto> findByPlaceId(Long placeId) {
        return repo.findByPlaceId(placeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WindowDto> getAll() {
        return List.of();
    }
}
