package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.domain.dto.LoginUserDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.impl.AuthenticationService;
import com.example.electronic_queue_monolit.service.impl.JwtService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String loginPage(Model model, 
                           @RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout) {
        System.out.println("Запрошена страница логина GET /login" + 
                           (error != null ? " с ошибкой" : "") + 
                           (logout != null ? " после выхода" : ""));
        
        if (model.getAttribute("loginForm") == null) {
            model.addAttribute("loginForm", new LoginUserDto());
        }
        
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginUserDto loginUserDto,
                        HttpServletResponse response,
                        Model model) {
        try {
            if (loginUserDto.getUsername() == null ||  loginUserDto.getPassword() == null) {
                model.addAttribute("error", "Все поля формы должны быть заполнены");
                return "login";
            }

                
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            System.out.println("Пользователь аутентифицирован: " + authenticatedUser.getUsername());

            String jwtToken = jwtService.generateToken(authenticatedUser);
            System.out.println("Токен создан: " + jwtToken.substring(0, Math.min(20, jwtToken.length())) + "...");

            Cookie jwtCookie = new Cookie("authToken", jwtToken);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge((int) (jwtService.getExpirationTime() / 1000));
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            response.addCookie(jwtCookie);
            System.out.println("Куки добавлена: " + jwtCookie.getName() + " с путем: " + jwtCookie.getPath() + 
                ", httpOnly: " + jwtCookie.isHttpOnly() + ", secure: " + jwtCookie.getSecure() +
                ", maxAge: " + jwtCookie.getMaxAge());
            
            response.setHeader("Authorization", "Bearer " + jwtToken);
            System.out.println("Токен также добавлен в заголовок Authorization");

            return "redirect:/admin-overview";
        } catch (Exception e) {
            System.out.println("Ошибка аутентификации: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Неверное ФИО или пароль");
            model.addAttribute("loginForm", loginUserDto);
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("authToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        System.out.println("Куки authToken удалена при выходе из системы");
        
        return "redirect:/login?logout=true";
    }
} 