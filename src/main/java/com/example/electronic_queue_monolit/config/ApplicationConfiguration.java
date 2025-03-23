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
            if (username == null || username.trim().isEmpty()) {
                throw new UsernameNotFoundException("Имя пользователя не может быть пустым");
            }
            
            String[] parts = username.split(" ");
            if (parts.length == 3) {
                String surname = parts[0];
                String name = parts[1];
                String patronymic = parts[2];
                try {
                    return userRepository.findBySurnameAndNameAndPatronymic(surname, name, patronymic)
                            .orElseThrow(() -> {
                                return new UsernameNotFoundException("Пользователь не найден");
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
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
