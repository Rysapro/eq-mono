package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.WindowDto;
import com.example.electronic_queue_monolit.domain.model.Window;
import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.UserService;
import com.example.electronic_queue_monolit.service.WindowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/win")
@Tag(name = "Окно")
public class WindowController extends BaseController<Window, WindowDto, WindowService> {

    private final UserService userService;
    private final PlaceService placeService;

    public WindowController(WindowService windowService, UserService userService, PlaceService placeService) {
        super(windowService);
        this.userService = userService;
        this.placeService = placeService;
    }

    @Override
    protected String getBasePath() {
        return "win";
    }

    @Override
    protected WindowDto createEmptyDto() {
        return new WindowDto();
    }
    
    @Override
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        WindowDto dto = baseService.findById(id);
        model.addAttribute("form", dto);
        model.addAttribute("operators", userService.findAll());
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/edit";
    }
    
    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", createEmptyDto());
        model.addAttribute("operators", userService.findAll());
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/create";
    }
}
