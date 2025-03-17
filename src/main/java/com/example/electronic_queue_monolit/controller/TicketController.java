package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.model.Ticket;
import com.example.electronic_queue_monolit.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ticket")
@Tag(name = "Талон")
public class TicketController extends BaseController<Ticket, TicketDto, TicketService> {

    private final TicketStatusService ticketStatusService;
    private final ProvisionService provisionService;
    private final PlaceService placeService;
    private final InformationService informationService;

    public TicketController(TicketService ticketService, 
                           TicketStatusService ticketStatusService,
                           ProvisionService provisionService,
                           PlaceService placeService,
                           InformationService informationService) {
        super(ticketService);
        this.ticketStatusService = ticketStatusService;
        this.provisionService = provisionService;
        this.placeService = placeService;
        this.informationService = informationService;
    }

    @Override
    protected String getBasePath() {
        return "ticket";
    }

    @Override
    protected TicketDto createEmptyDto() {
        return new TicketDto(null, null, null, null, null, null, null);
    }
    
    @Override
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        TicketDto dto = baseService.findById(id);
        model.addAttribute("form", dto);
        model.addAttribute("statuses", ticketStatusService.findAll());
        model.addAttribute("provisions", provisionService.findAll());
        model.addAttribute("places", placeService.findAll());

        return getBasePath() + "/edit";
    }
    
    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", createEmptyDto());
        model.addAttribute("statuses", ticketStatusService.findAll());
        model.addAttribute("provisions", provisionService.findAll());
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/create";
    }

    @GetMapping("/ticket-list-page")
    public String showList(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) Long informationId,
            @RequestParam(required = false) Long provisionId,
            @RequestParam(required = false) Long ticketStatusId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<TicketDto> tickets = baseService.getTickets(
                placeId, informationId, provisionId, ticketStatusId, pageable
        );
        
        // Добавляем данные для фильтров
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("provisions", provisionService.findAll());
        model.addAttribute("statuses", ticketStatusService.findAll());
        model.addAttribute("informations", informationService.findAll());
        
        // Добавляем выбранные значения фильтров
        model.addAttribute("placeId", placeId);
        model.addAttribute("informationId", informationId);
        model.addAttribute("provisionId", provisionId);
        model.addAttribute("ticketStatusId", ticketStatusId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("size", size);
        
        // Добавляем данные для пагинации
        model.addAttribute("items", tickets.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tickets.getTotalPages());
        model.addAttribute("totalItems", tickets.getTotalElements());
        
        return "ticket/ticket-list-page";
    }

    @GetMapping("/generate-form")
    public String generateTicketForm(Model model) {
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("provisions", provisionService.findAll());
        return "ticket/generate";
    }

    @GetMapping("/generate")
    public String generateTicket(
            @RequestParam Long placeId,
            @RequestParam Long provisionId
    ) {
        baseService.generateTicket(placeId, provisionId);
        return "redirect:/ticket/list";
    }
}