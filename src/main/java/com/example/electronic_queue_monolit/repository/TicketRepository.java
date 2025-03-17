package com.example.electronic_queue_monolit.repository;

import com.example.electronic_queue_monolit.domain.dto.OperatorTicketCountDto;
import com.example.electronic_queue_monolit.domain.model.Ticket;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends BaseRepository<Ticket> {
    @Query("SELECT MAX(t.ticketCode) FROM Ticket t WHERE t.place.code = :code AND t.provision.provisionName = :provisionName" )
    String findMaxTicketCodeFor(@Param("code") String code, @Param("provisionName") String provisionName);

    @Query("SELECT t FROM Ticket t WHERE (:placeId IS NULL OR t.place.id= :placeId) " +
            "AND (:informationId IS NULL OR t.information.id = :informationId) " +
            "AND (:provisionId IS NULL OR t.provision.id = :provisionId) " +
            "AND (:ticketStatusId IS NULL OR t.ticketStatus.id = :ticketStatusId)")
    Page<Ticket> findAll(@Param("placeId") Long place, @Param("informationId") Long information,
                         @Param("provisionId") Long provision, @Param("ticketStatusId") Long ticketStatus,
                         Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE (:placeId IS NULL OR t.place.id= :placeId)" +
            "AND (:ticketStatusId IS NULL OR t.ticketStatus.id = :ticketStatusId)" +
            "AND t.createdDate = CURRENT_DATE")
    Page<Ticket> findPlace(@Param("placeId") Long place, @Param("ticketStatusId") Long ticketStatus,
                           Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE (:placeId IS NULL OR t.place.id= :placeId)"+
            "AND (:ticketStatusId IS NULL OR t.ticketStatus.id = :ticketStatusId)")
    List<Ticket> findByPlaceId(@Param("placeId") Long place, @Param("ticketStatusId") Long ticketStatus);
/*    @Query("SELECT t FROM Ticket t WHERE (:placeId IS NULL OR t.place.id= :placeId) AND t.ticketStatus.id = 5 AND t.updateDate >= :fiveMinutesAgo")
    List<Ticket> getAllTicketStatusAbsence(@Param("placeId") Long place, @Param("fiveMinutesAgo") LocalDateTime fiveMinutesAgo);

    @Query("SELECT t FROM Ticket t WHERE t.ticketStatus.id = 3 AND t.updateDate BETWEEN :startDate AND :endDate")
    Page<Ticket> findAllFinishedTickets(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE (:placeId IS NULL OR t.place.id= :placeId) AND t.ticketStatus.id = 3 AND t.updateDate BETWEEN :startDate AND :endDate")
    Page<Ticket> findAllFinishedTicketsWithPlace(@Param("placeId") Long place, @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT new OperatorTicketCountDto(t.operatorId.name, COUNT(t)) " +
            "FROM Ticket t " +
            "WHERE t.ticketStatus.id = 3 " +
            "AND t.updateDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.operatorId.name")
    List<OperatorTicketCountDto> countProcessedTicketsByOperator(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );*/

}
