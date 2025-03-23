package com.example.electronic_queue_monolit.config;

import com.example.electronic_queue_monolit.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("UserDetailsService: поиск пользователя по имени: " + (username == null ? "null" : "'" + username + "'"));
            
            // Если имя пустое, сразу возвращаем ошибку
            if (username == null || username.trim().isEmpty()) {
                System.out.println("UserDetailsService: имя пользователя пустое или null");
                throw new UsernameNotFoundException("Имя пользователя не может быть пустым");
            }
            
            // Пробуем разобрать полное имя как "Фамилия Имя Отчество"
            String[] parts = username.split(" ");
            if (parts.length == 3) {
                String surname = parts[0];
                String name = parts[1];
                String patronymic = parts[2];
                System.out.println("UserDetailsService: разбор имени: фамилия=" + surname + ", имя=" + name + ", отчество=" + patronymic);
                
                try {
                    return userRepository.findBySurnameAndNameAndPatronymic(surname, name, patronymic)
                            .orElseThrow(() -> {
                                System.out.println("UserDetailsService: пользователь не найден по ФИО");
                                return new UsernameNotFoundException("Пользователь не найден");
                            });
                } catch (Exception e) {
                    System.out.println("UserDetailsService: ошибка при поиске пользователя по ФИО: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("UserDetailsService: имя пользователя не соответствует формату 'Фамилия Имя Отчество', частей: " + parts.length);
            }
            
            // Если не получилось по полному имени, попробуем поискать по логину
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // Используем NoOpPasswordEncoder для простой проверки пароля без хэширования
        // ВНИМАНИЕ: В производственной среде следует использовать BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
