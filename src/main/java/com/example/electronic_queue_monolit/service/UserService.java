package com.example.electronic_queue_monolit.service;

import com.example.electronic_queue_monolit.domain.dto.UserDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.base.BaseService;

public interface UserService extends BaseService<User, UserDto> {
    /**
     * Получает текущего аутентифицированного пользователя
     * @return текущий пользователь или null, если пользователь не аутентифицирован
     */
    User getCurrentUser();
}
