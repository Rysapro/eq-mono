package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.config.Counter;
import com.example.electronic_queue_monolit.domain.dto.*;
import com.example.electronic_queue_monolit.domain.model.*;
import com.example.electronic_queue_monolit.repository.PlaceRepository;
import com.example.electronic_queue_monolit.repository.ProvisionRepository;
import com.example.electronic_queue_monolit.repository.TicketRepository;
import com.example.electronic_queue_monolit.repository.TicketStatusRepository;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import com.example.electronic_queue_monolit.service.TicketService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl extends BaseServiceImpl<Ticket, TicketDto, TicketRepository> implements TicketService {

    private final PlaceRepository placeRepository;
    private final ProvisionRepository provisionRepository;
    private final TicketStatusRepository ticketStatusRepository;
    private final Counter counter;
    private final BaseRepository baseRepository;
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository repo, PlaceRepository placeRepository, ProvisionRepository provisionRepository, TicketStatusRepository ticketStatusRepository, Counter counter, @Qualifier("baseRepository") BaseRepository baseRepository, TicketRepository ticketRepository) {
        super(repo);
        this.placeRepository = placeRepository;
        this.provisionRepository = provisionRepository;
        this.ticketStatusRepository = ticketStatusRepository;
        this.counter = counter;
        this.baseRepository = baseRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket toEntity(TicketDto dto) {
        Ticket ticket = new Ticket();
        ticket.setNumber(dto.getNumber());
        ticket.setOperatorId(createEntityIfNotNull(
                dto.getOperatorId() != null ? dto.getOperatorId().getId() : null, id -> {
                    User user = new User();
                    user.setId(id);
                    return user;
                }
        ));
        ticket.setPlace(createEntityIfNotNull(
                dto.getPlace() != null ? dto.getPlace().getId() : null, id -> {
                    Place place = new Place();
                    place.setId(id);
                    return place;
                }
        ));
        ticket.setInformation(createEntityIfNotNull(
                dto.getInformation() != null ? dto.getInformation().getId() : null, id -> {
                    Information information = new Information();
                    information.setId(id);
                    return information;
                }
        ));
        ticket.setProvision(createEntityIfNotNull(
                dto.getProvision() != null ? dto.getProvision().getId() : null, id -> {
                    Provision provision = new Provision();
                    provision.setId(id);
                    return provision;
                }
        ));
        ticket.setTicketStatus(createEntityIfNotNull(
                dto.getTicketStatus() != null ? dto.getTicketStatus().getId() : null, id -> {
                    TicketStatus ticketStatus = new TicketStatus();
                    ticketStatus.setId(id);
                    return ticketStatus;
                }
        ));
        return ticket;
    }

    private <T> T createEntityIfNotNull(Long id, java.util.function.Function<Long, T> constructor) {
        return id != null ? constructor.apply(id) : null;
    }

    @Override
    public TicketDto toDTO(Ticket entity) {
        TicketDto dto = new TicketDto();
        dto.setId(entity.getId());
        dto.setNumber(entity.getNumber());

        if (entity.getOperatorId() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(entity.getOperatorId().getId());
            userDto.setName(entity.getOperatorId().getName());
            dto.setOperatorId(userDto);
        }
        
        if (entity.getPlace() != null) {
            PlaceDto placeDto = new PlaceDto();
            placeDto.setId(entity.getPlace().getId());
            placeDto.setName(entity.getPlace().getName());
            dto.setPlace(placeDto);
        }
        
        if (entity.getInformation() != null) {
            InformationDto informationDto = new InformationDto();
            informationDto.setId(entity.getInformation().getId());
            informationDto.setDeclaration(entity.getInformation().getDeclaration());
            dto.setInformation(informationDto);
        }
        
        if (entity.getProvision() != null) {
            ProvisionDto provisionDto = new ProvisionDto();
            provisionDto.setId(entity.getProvision().getId());
            provisionDto.setProvisionName(entity.getProvision().getProvisionName());
            dto.setProvision(provisionDto);
        }
        
        if (entity.getTicketStatus() != null) {
            TicketStatusDto ticketStatusDto = new TicketStatusDto();
            ticketStatusDto.setId(entity.getTicketStatus().getId());
            ticketStatusDto.setName(entity.getTicketStatus().getName());
            dto.setTicketStatus(ticketStatusDto);
        }
        
        return dto;
    }

    public String generateTicket(Long placeId, Long provisionId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("МТО не найдено"));
        Provision provision = provisionRepository.findById(provisionId)
                .orElseThrow(() -> new RuntimeException("Услуга не найдено"));
        TicketStatus status = ticketStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Статус с ID 1 не найден"));

        int ticketNumber = counter.getNextNumber();
        String number = generateTicketCode(place.getCode(), provision.getProvisionName(), ticketNumber);

        Ticket ticket = new Ticket();
        ticket.setNumber(number);
        ticket.setPlace(place);
        ticket.setProvision(provision);
        ticket.setTicketStatus(status);

        repo.save(ticket);

        return number;
    }

    public String generateTicketCode(String code, String provisionName, int ticketNumber) {
        String lastTicketCode = repo.findMaxTicketCodeFor(code, provisionName);
        String numberPart = (lastTicketCode == null) ? "0" : lastTicketCode.replaceAll("\\D", "");
        int lastNumber = numberPart.isEmpty() ? 0 : Integer.parseInt(numberPart);

        return String.format("%s-%s-%04d", code, provisionName, ticketNumber);
    }

    @Override
    public Ticket updateTicket(Ticket updatedTicket) {
        Ticket ticket = repo.findById(updatedTicket.getId())
                .orElseThrow(() -> new RuntimeException("Билет не найден!!!"));

        if (updatedTicket.getPlace() != null) {
            ticket.setPlace(updatedTicket.getPlace());
        }

        if (updatedTicket.getProvision() != null) {
            ticket.setProvision(updatedTicket.getProvision());
        }

        if (updatedTicket.getTicketStatus() != null) {
            ticket.setTicketStatus(updatedTicket.getTicketStatus());
        }

        repo.save(ticket);
        return ticket;
    }

    @Override
    public void deleteTicketById(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Билет не найден!");
        }
        repo.deleteById(id);
    }

    @Override
    public Page<TicketDto> getTickets(Long placeId, Long informationId, Long provisionId,
                                              Long ticketStatusId, Pageable pageable) {
        if (placeId == null && informationId == null && provisionId == null && ticketStatusId == null) {
            return Page.empty(pageable);
        } else {
            return ticketRepository.findAll(placeId, informationId, provisionId, ticketStatusId, pageable)
                    .map(this::mapToTicketResponseDto);
        }
    }

    public TicketDto mapToTicketResponseDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setNumber(ticket.getNumber());
        
        if (ticket.getOperatorId() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(ticket.getOperatorId().getId());
            userDto.setName(ticket.getOperatorId().getName());
            dto.setOperatorId(userDto);
        }
        
        if (ticket.getPlace() != null) {
            PlaceDto placeDto = new PlaceDto();
            placeDto.setId(ticket.getPlace().getId());
            placeDto.setName(ticket.getPlace().getName());
            dto.setPlace(placeDto);
        }
        
        if (ticket.getInformation() != null) {
            InformationDto informationDto = new InformationDto();
            informationDto.setId(ticket.getInformation().getId());
            informationDto.setDeclaration(ticket.getInformation().getDeclaration());
            dto.setInformation(informationDto);
        }
        
        if (ticket.getProvision() != null) {
            ProvisionDto provisionDto = new ProvisionDto();
            provisionDto.setId(ticket.getProvision().getId());
            provisionDto.setProvisionName(ticket.getProvision().getProvisionName());
            dto.setProvision(provisionDto);
        }
        
        if (ticket.getTicketStatus() != null) {
            TicketStatusDto ticketStatusDto = new TicketStatusDto();
            ticketStatusDto.setId(ticket.getTicketStatus().getId());
            ticketStatusDto.setName(ticket.getTicketStatus().getName());
            dto.setTicketStatus(ticketStatusDto);
        }
        
        return dto;
    }

    @Override
    public Page<TicketDto> getPlaceId(Long placeId, Long ticketStatusId, Pageable pageable) {
        if (placeId == null && ticketStatusId == null) {
            return Page.empty(pageable);
        } else {
            return ticketRepository.findPlace(placeId, ticketStatusId, pageable)
                    .map(this::mapToTicketResponseDto);
        }
    }

    @Override
    public List<TicketDto> getAllPlaceId(Long placeId, Long ticketStatusId) {
        return ticketRepository.findByPlaceId(placeId, ticketStatusId)
                .stream()
                .map(this::mapToTicketResponseDto)
                .collect(Collectors.toList());
    }
}
