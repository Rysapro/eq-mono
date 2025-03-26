package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.ProvisionService;
import com.example.electronic_queue_monolit.service.TicketService;
import com.example.electronic_queue_monolit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Tag(name = "Главная страница")
public class MainController {

    private final TicketService ticketService;
    private final UserService userService;
    private final PlaceService placeService;
    private final ProvisionService provisionService;

    public MainController(TicketService ticketService, UserService userService, PlaceService placeService, ProvisionService provisionService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.placeService = placeService;
        this.provisionService = provisionService;
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
        List<TicketDto> activeTickets = ticketService.getTicketsByStatus(2L);
        
        Map<PlaceDto, List<TicketDto>> ticketsByPlace = new HashMap<>();
        for (TicketDto ticket : activeTickets) {
            PlaceDto place = ticket.getPlace();
            if (!ticketsByPlace.containsKey(place)) {
                ticketsByPlace.put(place, new ArrayList<>());
            }
            ticketsByPlace.get(place).add(ticket);
        }
        
        Map<PlaceDto, List<Map<String, Object>>> ticketsWithWindowInfo = new HashMap<>();
        
        for (Map.Entry<PlaceDto, List<TicketDto>> entry : ticketsByPlace.entrySet()) {
            PlaceDto place = entry.getKey();
            List<TicketDto> tickets = entry.getValue();
            
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