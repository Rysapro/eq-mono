package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.RoleDto;
import com.example.electronic_queue_monolit.domain.model.Role;
import com.example.electronic_queue_monolit.service.PageService;
import com.example.electronic_queue_monolit.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/role")
@Tag(name = "Роль")
public class RoleController extends BaseController<Role, RoleDto, RoleService> {

    private final PageService pageService;

    public RoleController(RoleService service, PageService pageService) {
        super(service);
        this.pageService = pageService;
    }

    @Override
    protected String getBasePath() {
        return "role";
    }

    @Override
    protected RoleDto createEmptyDto() {
        return new RoleDto(null, null, null, null);
    }
    
    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", createEmptyDto());
        model.addAttribute("pages", pageService.findAll());
        return getBasePath() + "/create";
    }
    
    @Override
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        RoleDto dto = baseService.findById(id);
        model.addAttribute("form", dto);
        model.addAttribute("pages", pageService.findAll());
        return getBasePath() + "/edit";
    }
}