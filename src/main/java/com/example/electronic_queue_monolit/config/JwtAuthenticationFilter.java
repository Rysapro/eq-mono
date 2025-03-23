package com.example.electronic_queue_monolit.config;

import com.example.electronic_queue_monolit.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("===================================");
        System.out.println("Обработка запроса: " + requestURI);
        
        // Игнорируем URLs для логина, статических ресурсов 
        if (shouldSkipAuthentication(requestURI)) {
            System.out.println("Пропуск аутентификации для пути: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        
        // Ищем токен в заголовке и куки
        String jwt = extractTokenFromRequest(request);
        
        // Если токен не найден, перенаправляем на страницу логина
        if (jwt == null) {
            System.out.println("Токен не найден в запросе. Перенаправление на страницу логина.");
            response.sendRedirect("/login");
            return;
        }

        try {
            final String username = jwtService.extractUsername(jwt);
            System.out.println("Извлечено имя пользователя из токена: " + username);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                try {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    System.out.println("Пользователь найден в БД: " + userDetails.getUsername());
                    
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println("Аутентификация успешно установлена для пользователя: " + username);
                    } else {
                        System.out.println("Токен недействителен");
                        response.sendRedirect("/login");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при поиске пользователя: " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("/login");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            System.out.println("Ошибка в фильтре JWT: " + exception.getMessage());
            exception.printStackTrace();
            
            // Перенаправление на страницу логина при ошибках
            response.sendRedirect("/login");
        }
    }
    
    private boolean shouldSkipAuthentication(String requestURI) {
        return requestURI.equals("/login") || 
               requestURI.startsWith("/css/") || 
               requestURI.startsWith("/js/") || 
               requestURI.startsWith("/img/") ||
               requestURI.startsWith("/images/") ||
               requestURI.equals("/favicon.ico") ||
               requestURI.equals("/logout") ||
               requestURI.startsWith("/auth/") ||
               requestURI.equals("/quest") ||
               requestURI.equals("/active-tickets");
    }
    
    private String extractTokenFromRequest(HttpServletRequest request) {
        // Сначала пробуем получить токен из заголовка Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Токен найден в заголовке: " + token.substring(0, Math.min(10, token.length())) + "...");
            return token;
        }
        
        // Если токена в заголовке нет, проверяем куки
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("Найдено куки: " + cookies.length);
            for (Cookie cookie : cookies) {
                System.out.println("Куки: " + cookie.getName() + " = " + 
                    (cookie.getValue() != null ? cookie.getValue().substring(0, Math.min(10, cookie.getValue().length())) + "..." : "null"));
                
                if ("authToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    System.out.println("Токен найден в куки: " + cookie.getValue().substring(0, Math.min(10, cookie.getValue().length())) + "...");
                    return cookie.getValue();
                }
            }
            
            System.out.println("Куки authToken не найдена среди: " + 
                Arrays.stream(cookies).map(Cookie::getName).reduce("", (a, b) -> a + ", " + b));
        } else {
            System.out.println("Куки отсутствуют в запросе");
        }
        
        System.out.println("Токен не найден ни в заголовке, ни в куки");
        return null;
    }
}
