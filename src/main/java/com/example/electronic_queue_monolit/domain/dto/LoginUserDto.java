package com.example.electronic_queue_monolit.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginUserDto {
    private String name;
    private String surname;
    private String patronymic;
    private String password;
}
