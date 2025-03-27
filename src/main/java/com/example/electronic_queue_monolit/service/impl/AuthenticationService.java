package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.LoginUserDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(LoginUserDto input) {

        User user = userRepository.findByUsername(
                input.getUsername()
        ).orElseThrow(() -> {
            System.out.println("Пользователь не найден по login: " + input.getUsername());
            return new BadCredentialsException("Неверное login или пароль");
        });
        
        if (!input.getPassword().equals(user.getPassword())) {
            System.out.println("Пароль не совпадает");
            System.out.println("Введенный пароль: " + input.getPassword());
            System.out.println("Сохраненный пароль: " + user.getPassword());
            throw new BadCredentialsException("Неверное login или пароль");
        }
        
        System.out.println("Аутентификация успешна");
        return user;
    }
}
