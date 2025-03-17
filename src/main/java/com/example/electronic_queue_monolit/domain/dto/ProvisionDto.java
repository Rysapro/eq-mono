package com.example.electronic_queue_monolit.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProvisionDto{
    private Long id;
    private String provisionName;
    private String code;
    private PlaceDto place;
}