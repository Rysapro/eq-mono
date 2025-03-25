package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.domain.dto.LoginUserDto;
import com.example.electronic_queue_monolit.domain.dto.RegisterUserDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.impl.AuthenticationService;
import com.example.electronic_queue_monolit.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("registerForm", new RegisterUserDto());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String register(@ModelAttribute("registerForm") RegisterUserDto registerUserDto,
                          Model model) {
        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "auth/signup";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "registered", required = false) String registered) {
        if (model.getAttribute("loginForm") == null) {
            model.addAttribute("loginForm", new LoginUserDto());
        }
        
        if (error != null) {
            model.addAttribute("error", "Неверные учетные данные");
        }
        
        if (registered != null) {
            model.addAttribute("success", "Регистрация успешна! Теперь вы можете войти.");
        }
        
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticate(@ModelAttribute("loginForm") LoginUserDto loginUserDto,
                             HttpServletResponse response,
                             Model model) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            // Сохраняем токен в куки
            Cookie jwtCookie = new Cookie("authToken", jwtToken);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge((int) (jwtService.getExpirationTime() / 1000));
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            response.addCookie(jwtCookie);

            // Добавляем токен в заголовок
            response.setHeader("Authorization", "Bearer " + jwtToken);

            return "redirect:/admin-overview";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка аутентификации: " + e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("authToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        return "redirect:/auth/login?logout=true";
    }
}
