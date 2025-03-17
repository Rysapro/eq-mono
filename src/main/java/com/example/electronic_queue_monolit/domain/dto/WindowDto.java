package com.example.electronic_queue_monolit.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WindowDto {
    private Long id;
    private String number;
    private TicketDto processedTicket;
    private UserDto operator;
    private PlaceDto place;
} 