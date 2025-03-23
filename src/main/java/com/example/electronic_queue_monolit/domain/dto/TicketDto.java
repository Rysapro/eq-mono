package com.example.electronic_queue_monolit.domain.dto;

import com.example.electronic_queue_monolit.domain.model.TicketStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketDto{
    private Long id;
    private String number;
    private UserDto operatorId;
    private ProvisionDto provision;
    private PlaceDto place;
    private InformationDto information;
    private TicketStatusDto ticketStatus;
    private WindowDto window;
}