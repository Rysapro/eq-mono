package com.example.electronic_queue_monolit.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto{
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String password;
    private Boolean blocked;
    private PlaceDto place;
    private RoleDto role;
}