package com.example.electronic_queue_monolit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        System.out.println("SecurityConfiguration инициализирована");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Настройка цепочки фильтров безопасности");
        
        http
                .csrf(csrf -> {
                    csrf.disable();
                    System.out.println("CSRF защита отключена");
                })
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                    System.out.println("CORS настроен");
                })
                .authorizeHttpRequests(auth -> {
                    // Публичные URL
                    auth.requestMatchers("/login", "/auth/login", "/logout").permitAll();
                    auth.requestMatchers("/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**").permitAll();
                    auth.requestMatchers("/swagger-ui.html").permitAll();
                    auth.requestMatchers("/quest").permitAll();
                    auth.requestMatchers("/active-tickets").permitAll();

                    // Разрешаем доступ к статическим ресурсам без аутентификации
                    auth.requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll();
                    
                    // Все остальные запросы требуют аутентификации
                    auth.anyRequest().authenticated();
                    System.out.println("Настроены правила доступа");
                })
                // Отключаем автоматическую форму входа Spring Security 
                // и настраиваем нашу пользовательскую форму
                .formLogin(form -> {
                    form.disable(); // Отключаем стандартную форму Spring Security
                    System.out.println("Стандартная форма логина отключена");
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");  // URL для выхода из системы
                    logout.logoutSuccessUrl("/login?logout=true");  // URL для перенаправления после выхода
                    logout.deleteCookies("authToken");  // Удаление куки при выходе
                    logout.permitAll();
                    System.out.println("Настроен выход из системы");
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    System.out.println("Настроено управление сессиями: STATELESS");
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("Цепочка фильтров безопасности настроена");
        return http.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5321"));
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type", "Cache-Control"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}

