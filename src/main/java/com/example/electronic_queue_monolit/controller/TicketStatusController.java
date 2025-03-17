package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.TicketStatusDto;
import com.example.electronic_queue_monolit.domain.model.TicketStatus;
import com.example.electronic_queue_monolit.service.TicketStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/status")
@Tag(name = "Статус талона")
public class TicketStatusController extends BaseController<TicketStatus, TicketStatusDto, TicketStatusService> {

    public TicketStatusController(TicketStatusService ticketStatusService) {
        super(ticketStatusService);
    }

    @Override
    protected String getBasePath() {
        return "status";
    }

    @Override
    protected TicketStatusDto createEmptyDto() {
        return new TicketStatusDto(null,null);
    }
}
