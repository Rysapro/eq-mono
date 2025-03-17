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
  /*
    // Методы для работы с талонами
    TicketResponseDto acceptanceTicket(Long id);
    
    TicketResponseDto completionTicket(Long id);
    
    TicketResponseDto absenceTicket(Long id);
    
    TicketResponseDto getAbsenceTicket(Long id);
    
    List<TicketResponseDto> getAllTicketStatusAbsence(Long placeId);
    
    Page<TicketResponseDto> getTicketStatusFinished(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<TicketResponseDto> getTicketStatusFinishedWithPlace(Long placeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<OperatorTicketCountDto> getCountTicketByUser(LocalDateTime startDate, LocalDateTime endDate);*/
}
