package com.example.electronic_queue_monolit.service;

import com.example.electronic_queue_monolit.domain.dto.UserDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.base.BaseService;

import java.util.List;

public interface UserService extends BaseService<User, UserDto> {
    List<User> searchUser(String searchWord);
}
