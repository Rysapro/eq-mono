package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.dto.WindowDto;
import com.example.electronic_queue_monolit.controller.base.WebBaseController;
import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.ProvisionService;
import com.example.electronic_queue_monolit.service.TicketService;
import com.example.electronic_queue_monolit.service.UserService;
import com.example.electronic_queue_monolit.service.WindowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Tag(name = "Главная страница")
public class MainController extends WebBaseController {

    private final TicketService ticketService;
    private final UserService userService;
    private final PlaceService placeService;
    private final ProvisionService provisionService;
    private final WindowService windowService;

    public MainController(TicketService ticketService, UserService userService, PlaceService placeService, ProvisionService provisionService, WindowService windowService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.placeService = placeService;
        this.provisionService = provisionService;
        this.windowService = windowService;
    }

    @GetMapping("/admin-overview")
    public String admin(Model model) {
        model.addAttribute("ticketCount", ticketService.findAll().size());
        model.addAttribute("activeTicketCount", 0);
        model.addAttribute("userCount", userService.findAll().size());
        model.addAttribute("placeCount", placeService.findAll().size());
        
        return "admin";
    }

    @GetMapping("/quest")
    public String index(Model model) {
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("provisions", provisionService.findAll());
        return "select";
    }

    @GetMapping("/active-tickets")
    public String activeTickets(Model model) {
        // Получаем билеты со статусом 2 (активные)
        List<TicketDto> activeTickets = ticketService.getTicketsByStatus(2L);
        
        // Группируем билеты по местам (Place)
        Map<PlaceDto, List<TicketDto>> ticketsByPlace = new HashMap<>();
        for (TicketDto ticket : activeTickets) {
            PlaceDto place = ticket.getPlace();
            if (!ticketsByPlace.containsKey(place)) {
                ticketsByPlace.put(place, new ArrayList<>());
            }
            ticketsByPlace.get(place).add(ticket);
        }
        
        // Создаем карту для хранения информации о билетах
        Map<PlaceDto, List<Map<String, Object>>> ticketsWithWindowInfo = new HashMap<>();
        
        // Для каждого места и списка билетов
        for (Map.Entry<PlaceDto, List<TicketDto>> entry : ticketsByPlace.entrySet()) {
            PlaceDto place = entry.getKey();
            List<TicketDto> tickets = entry.getValue();
            
            // Создаем список с информацией о билетах для этого места
            List<Map<String, Object>> ticketInfoList = new ArrayList<>();
            
            for (TicketDto ticket : tickets) {
                Map<String, Object> ticketInfo = new HashMap<>();
                ticketInfo.put("ticket", ticket);
                ticketInfoList.add(ticketInfo);
            }
            
            ticketsWithWindowInfo.put(place, ticketInfoList);
        }
        
        model.addAttribute("activeTicketsByPlace", ticketsWithWindowInfo);
        return "quest/active-tickets";
    }
} 