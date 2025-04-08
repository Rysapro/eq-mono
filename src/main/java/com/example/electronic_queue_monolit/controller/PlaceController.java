package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.PlaceTicketCountDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.service.PlaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/place")
@Tag(name = "Место таможенного оформления")
public class PlaceController extends BaseController<Place, PlaceDto, PlaceService> {

    public PlaceController(PlaceService service) {
        super(service);
    }

    @GetMapping("/ticket-count")
    @ResponseBody
    public List<PlaceTicketCountDto> getPending() {
        return baseService.getPending();
    }

    @GetMapping("/ticket-count-page")
    public String ticketCountPage() {
        return "place/ticket-count";
    }

    @Override
    protected String getBasePath() {
        return "place";
    }

    @Override
    protected PlaceDto createEmptyDto() {
        return new PlaceDto(null, null, null, null);
    }


}