package com.example.electronic_queue_monolit.controller.base;


import com.example.electronic_queue_monolit.service.base.BaseService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<ENTITY, DTO, SERVICE extends BaseService<ENTITY, DTO>> extends WebBaseController {

    protected final SERVICE baseService;
    protected abstract String getBasePath();
    protected abstract DTO createEmptyDto();

    public BaseController(SERVICE service) {
        this.baseService = service;
    }

    @GetMapping("/findById/{id}")
    public String findById(@PathVariable Long id, Model model) {
        DTO dto = baseService.findById(id);
        model.addAttribute("item", dto);
        return getBasePath() + "/view";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<DTO> allItems = baseService.findAll();
        model.addAttribute("items", allItems);
        return getBasePath() + "/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", createEmptyDto());
        return getBasePath() + "/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("form") DTO model) {
        baseService.create(model);
        return "redirect:/" + getBasePath() + "/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        DTO dto = baseService.findById(id);
        model.addAttribute("form", dto);
        return getBasePath() + "/edit";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") DTO model) {
        baseService.update(id, model);
        return "redirect:/" + getBasePath() + "/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        baseService.deleteById(id);
        return "redirect:/" + getBasePath() + "/list";
    }
}