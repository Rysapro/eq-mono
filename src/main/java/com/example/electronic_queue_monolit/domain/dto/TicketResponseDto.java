package com.example.electronic_queue_monolit.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketResponseDto {
    private Long id;
    private String number;
    private UserDto operatorId;
    private ProvisionDto provision;
    private PlaceDto place;
    private InformationDto information;
    private TicketStatusDto ticketStatus;
    private String processingTime;
} 