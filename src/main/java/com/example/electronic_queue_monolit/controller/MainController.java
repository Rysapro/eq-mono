package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
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
        return "/select";
    }

} 