package com.example.electronic_queue_monolit.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PlaceDto{
    private Long id;
    private String name;
    private String address;
    private String code;
}