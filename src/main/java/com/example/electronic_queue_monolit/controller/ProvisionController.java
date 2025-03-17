package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.ProvisionDto;
import com.example.electronic_queue_monolit.domain.model.Provision;
import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.ProvisionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provision")
@Tag(name = "Услуги")
public class ProvisionController extends BaseController<Provision, ProvisionDto, ProvisionService> {

    private final PlaceService placeService;

    public ProvisionController(ProvisionService service, PlaceService placeService) {
        super(service);
        this.placeService = placeService;
    }

    @Override
    protected String getBasePath() {
        return "provision";
    }

    @Override
    protected ProvisionDto createEmptyDto() {
        return new ProvisionDto(null, null, null, null);
    }
    
    @Override
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ProvisionDto dto = baseService.findById(id);
        model.addAttribute("form", dto);
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/edit";
    }
    
    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", createEmptyDto());
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/create";
    }
}
