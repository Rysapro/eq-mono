package com.example.electronic_queue_monolit.service;

import com.example.electronic_queue_monolit.domain.dto.OperatorTicketCountDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.dto.TicketResponseDto;
import com.example.electronic_queue_monolit.domain.model.Ticket;
import com.example.electronic_queue_monolit.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService extends BaseService<Ticket, TicketDto> {
    String generateTicket(Long placeId, Long provisionId);

    Ticket updateTicket(Ticket updatedTicket);

    void deleteTicketById(Long id);

    Page<TicketDto> getTickets(Long placeId, Long informationId, Long provisionId,
                                       Long ticketStatusId, Pageable pageable);
    Page<TicketDto> getPlaceId(Long placeId, Long ticketStatusId, Pageable pageable);

    List<TicketDto> getAllPlaceId(Long placeId, Long ticketStatusId);
    // Методы для работы с талонами
    TicketDto acceptanceTicket(Long id, Long windowId);
    
    TicketDto completionTicket(Long id);
    
    TicketDto absenceTicket(Long id);
    
    TicketDto getAbsenceTicket(Long id);
    
    List<TicketDto> getAllTicketStatusAbsence(Long placeId);
    
    Page<TicketDto> getTicketStatusFinished(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<TicketDto> getTicketStatusFinishedWithPlace(Long placeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<OperatorTicketCountDto> getCountTicketByUser(LocalDateTime startDate, LocalDateTime endDate);

    List<TicketDto> getTicketsByStatus(Long id);
    /*
    // Методы для работы с окнами
    TicketDto assignToWindow(Long ticketId, Long windowId);
    
    TicketDto assignNextTicketToWindow(Long windowId);*/
}
