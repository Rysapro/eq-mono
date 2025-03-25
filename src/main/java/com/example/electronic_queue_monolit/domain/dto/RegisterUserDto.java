package com.example.electronic_queue_monolit.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class RegisterUserDto {
    private String username;
    private String password;
    private RoleDto role;


}
