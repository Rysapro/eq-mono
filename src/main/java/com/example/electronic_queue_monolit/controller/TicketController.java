package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.OperatorTicketCountDto;
import com.example.electronic_queue_monolit.domain.dto.TicketDto;
import com.example.electronic_queue_monolit.domain.dto.WindowDto;
import com.example.electronic_queue_monolit.domain.model.Ticket;
import com.example.electronic_queue_monolit.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ticket")
@Tag(name = "Талон")
public class TicketController extends BaseController<Ticket, TicketDto, TicketService> {

    private final TicketStatusService ticketStatusService;
    private final ProvisionService provisionService;
    private final PlaceService placeService;
    private final InformationService informationService;
    private final WindowService windowService;

    public TicketController(TicketService ticketService, 
                           TicketStatusService ticketStatusService,
                           ProvisionService provisionService,
                           PlaceService placeService,
                           InformationService informationService,
                           WindowService windowService) {
        super(ticketService);
        this.ticketStatusService = ticketStatusService;
        this.provisionService = provisionService;
        this.placeService = placeService;
        this.informationService = informationService;
        this.windowService = windowService;
    }

    @Override
    protected String getBasePath() {
        return "ticket";
    }

    @Override
    protected TicketDto createEmptyDto() {
        return new TicketDto(null, null, null, null, null, null, null, null,null);
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
        
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("provisions", provisionService.findAll());
        model.addAttribute("statuses", ticketStatusService.findAll());
        model.addAttribute("informations", informationService.findAll());
        
        model.addAttribute("placeId", placeId);
        model.addAttribute("informationId", informationId);
        model.addAttribute("provisionId", provisionId);
        model.addAttribute("ticketStatusId", ticketStatusId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("size", size);
        
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

    @GetMapping("/select/generate")
    public String generateTicket(
            @RequestParam Long placeId,
            @RequestParam Long provisionId
    ) {
        baseService.generateTicket(placeId, provisionId);
        return "redirect:/ticket/your-ticket";
    }

    @GetMapping("/accept/{id}")
    public String acceptTicket(@PathVariable Long id, @RequestParam Long windowId, RedirectAttributes redirectAttributes) {
        try {
            TicketDto ticket = baseService.acceptanceTicket(id, windowId);
            redirectAttributes.addFlashAttribute("successMessage", "Талон успешно принят");
            return "redirect:/ticket/operator-panel?ticketStatusId=2";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/ticket/operator-panel";
        }
    }
    
    @GetMapping("/complete/{id}")
    public String completeTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            TicketDto ticket = baseService.completionTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "Обработка талона успешно завершена");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/ticket/operator-panel";
    }
    
    @GetMapping("/absence/{id}")
    public String markAbsent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            TicketDto ticket = baseService.absenceTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "Талон помечен как 'Клиент отсутствует'");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/ticket/operator-panel";
    }
    
    @GetMapping("/accept-absence/{id}")
    public String acceptAbsence(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            TicketDto ticket = baseService.getAbsenceTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "Клиент принят из списка отсутствующих");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/ticket/operator-panel";
    }
    
    @GetMapping("/operator-panel")
    public String operatorPanel(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) Long ticketStatusId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<TicketDto> tickets = baseService.getPlaceId(placeId, ticketStatusId, pageable);
        
        // Добавляем данные для фильтров
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("statuses", ticketStatusService.findAll());
        
        // Добавляем список доступных окон для выбора
        model.addAttribute("windows", windowService.findAll());
        
        // Добавляем выбранные значения фильтров
        model.addAttribute("placeId", placeId);
        model.addAttribute("ticketStatusId", ticketStatusId);
        
        // Добавляем данные для отображения
        model.addAttribute("tickets", tickets.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tickets.getTotalPages());
        model.addAttribute("totalItems", tickets.getTotalElements());
        
        return "ticket/operator-panel";
    }
    
    @GetMapping("/select/services")
    public String showServices(@RequestParam Long placeId, Model model) {
        model.addAttribute("place", placeService.findById(placeId));
        model.addAttribute("provisions", provisionService.findAll());
        return "services";
    }
    
    @GetMapping("/recent-absences")
    public String recentAbsences(
            Model model,
            @RequestParam(required = false) Long placeId
    ) {
        List<TicketDto> absences = baseService.getAllTicketStatusAbsence(placeId);
        
        model.addAttribute("absences", absences);
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("placeId", placeId);
        
        return "ticket/recent-absences";
    }
    
    @GetMapping("/finished-tickets")
    public String finishedTickets(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketDto> tickets = baseService.getTicketStatusFinished(startDate, endDate, pageable);
        
        model.addAttribute("tickets", tickets.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tickets.getTotalPages());
        model.addAttribute("totalItems", tickets.getTotalElements());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "ticket/finished-tickets";
    }
    
    @GetMapping("/finished-tickets-by-place")
    public String finishedTicketsByPlace(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketDto> tickets = baseService.getTicketStatusFinishedWithPlace(placeId, startDate, endDate, pageable);
        
        model.addAttribute("tickets", tickets.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tickets.getTotalPages());
        model.addAttribute("totalItems", tickets.getTotalElements());
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("placeId", placeId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "ticket/finished-tickets-by-place";
    }
    
    @GetMapping("/operator-statistics")
    public String operatorStatistics(
            Model model,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<OperatorTicketCountDto> statistics = baseService.getCountTicketByUser(startDate, endDate);
        
        model.addAttribute("statistics", statistics);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "ticket/operator-statistics";
    }
    
    @GetMapping("/operator-statistics-by-place")
    public String operatorStatisticsByPlace(
            Model model,
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<OperatorTicketCountDto> statistics = baseService.getCountTicketByUser(startDate, endDate);
        
        model.addAttribute("statistics", statistics);
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("placeId", placeId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "ticket/operator-statistics-by-place";
    }
    
    @GetMapping("/window-panel")
    public String windowPanel(
            Model model,
            @RequestParam(required = false) Long placeId
    ) {
        List<WindowDto> windows;
        if (placeId != null) {
            windows = windowService.findByPlaceId(placeId);
        } else {
            windows = windowService.findAll();
        }
        
        model.addAttribute("windows", windows);
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("placeId", placeId);
        
        return "ticket/window-panel";
    }

    @GetMapping("/active-tickets")
    public String activeTickets(Model model) {
        List<TicketDto> activeTickets = baseService.activeTickets();
        List<TicketDto> todayTickets = baseService.getAllTicketsForToday();
        model.addAttribute("activeTicketsByPlace", activeTickets);
        model.addAttribute("todayTickets", todayTickets);
        return "quest/active-tickets";
    }

    @GetMapping("/your-ticket")
    public String yourTicket(Model model){
        List<TicketDto> activeTickets = baseService.activeTickets();
        if (!activeTickets.isEmpty()) {
            // Получаем последний сгенерированный билет
            TicketDto lastGeneratedTicket = activeTickets.get(activeTickets.size() - 1);
            model.addAttribute("ticket", lastGeneratedTicket);
        }
        model.addAttribute("activeTicketsByPlace", activeTickets);
        return "quest/your-ticket";
    }




}