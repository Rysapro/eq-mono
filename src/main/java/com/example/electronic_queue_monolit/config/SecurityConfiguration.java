package com.example.electronic_queue_monolit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


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
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login").permitAll()

                        .requestMatchers(
                                "/information/findBySearchName",
                                "/information/findAll",
                                "/information/findById/{id}"
                        ).hasAnyRole("OPERATOR", "ADMIN")
                        .requestMatchers("/api/information/**").hasAnyRole("ADMIN")

                        .requestMatchers("/page/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/place/findBySearchName",
                                "/place/findAll",
                                "/place/pending-count",
                                "/place/findById/{id}",
                                "/place/active-tickets",
                                "/place/getPlacesByObjectForPlacesId").permitAll()
                        .requestMatchers("/place/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/provision/findBySearchName",
                                "/provision/findAll",
                                "/provision/findById/{id}",
                                "/provision/by-place").permitAll()
                        .requestMatchers("/provision/**").hasAnyRole("ADMIN","OPERATOR")

                        .requestMatchers(
                                "/role/findBySearchName",
                                "/role/findAll",
                                "/role/findById/{id}").hasAnyRole("ADMIN","OPERATOR")
                        .requestMatchers("/role/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/ticket/active-tickets",
                                "/ticket/generate",
                                "/ticket/select/**",
                                "/ticket/pending-by-place-paged").permitAll()
                        .requestMatchers("/ticket/**").hasAnyRole("ADMIN","OPERATOR")

                        .requestMatchers(
                                "/ticket-statuses/findBySearchName",
                                "/ticket-statuses/findAll",
                                "/ticket-statuses/findById/{id}"
                        ).hasAnyRole("OPERATOR","ADMIN")
                        .requestMatchers("/ticket-statuses/**").hasRole("ADMIN")

                        .requestMatchers("/user/findBySearchName",
                                "/user/findAll",
                                "/user/findById/{id}").hasRole("ADMIN")
                        .requestMatchers("/user/save", "/user/update/{id}").hasAnyRole("ADMIN")
                        .requestMatchers("/user/delete/{id}").hasRole("ADMIN")

                        .requestMatchers(
                                "/window/findAll",
                                "/window/by-place").permitAll()
                        .requestMatchers(
                                "/window/findBySearchName",
                                "/window/findById/{id}"
                        ).hasAnyRole("ADMIN","OPERATOR")
                        .requestMatchers("/window/**").hasRole("ADMIN")

                        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()

                        .requestMatchers(
                                "/quest",
                                "/generate-form",
                                "/js/**", "/img/**", "/favicon.ico", "/static/css/**", "/css/**"
                        ).permitAll()

                        .requestMatchers("/endpoint", "/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> {
                    form.disable(); // Отключаем стандартную форму Spring Security
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");  // URL для выхода из системы
                    logout.logoutSuccessUrl("/login?logout=true");  // URL для перенаправления после выхода
                    logout.deleteCookies("authToken");  // Удаление куки при выходе
                    logout.permitAll();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(STATELESS);
                })
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

