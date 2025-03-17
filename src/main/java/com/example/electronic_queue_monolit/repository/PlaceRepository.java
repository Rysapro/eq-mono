package com.example.electronic_queue_monolit.repository;

import com.example.electronic_queue_monolit.domain.dto.PlaceTicketCountDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends BaseRepository<Place> {
    @Query("SELECT new com.example.electronic_queue_monolit.domain.dto.PlaceTicketCountDto(p.name, COUNT(t)) " +
            "FROM Ticket t " +
            "JOIN t.place p " +
            "WHERE t.ticketStatus.id = 1 " +
            "GROUP BY p.name")
    List<PlaceTicketCountDto> countActiveTicketsPerPlace();
}
