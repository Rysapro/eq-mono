package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.TicketService;
import com.example.electronic_queue_monolit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Главная страница")
public class MainController {

    private final TicketService ticketService;
    private final UserService userService;
    private final PlaceService placeService;

    public MainController(TicketService ticketService, UserService userService, PlaceService placeService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Здесь можно добавить логику для получения статистики
        // Например, количество талонов, пользователей и т.д.
        
        // Пример заглушки для статистики
        model.addAttribute("ticketCount", ticketService.findAll().size());
        model.addAttribute("activeTicketCount", 0); // Здесь можно добавить логику для подсчета активных талонов
        model.addAttribute("userCount", userService.findAll().size());
        model.addAttribute("placeCount", placeService.findAll().size());
        
        return "index";
    }
} 