package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.RoleDto;
import com.example.electronic_queue_monolit.domain.dto.UserDto;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.PlaceService;
import com.example.electronic_queue_monolit.service.RoleService;
import com.example.electronic_queue_monolit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
@Tag(name = "Пользователь")
public class UserController extends BaseController<User, UserDto, UserService> {

    private final RoleService roleService;
    private final PlaceService placeService;

    public UserController(UserService userService, RoleService roleService, PlaceService placeService) {
        super(userService);
        this.roleService = roleService;
        this.placeService = placeService;
    }

    @Override
    protected String getBasePath() {
        return "user";
    }

    @Override
    protected UserDto createEmptyDto() {
        UserDto dto = new UserDto(null, null, null, null, null, null, false, null, null);
        dto.setPlace(new PlaceDto());
        dto.setRole(new RoleDto());
        return dto;
    }
    
    @Override
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        UserDto dto = baseService.findById(id);
        if (dto.getPlace() == null) {
            dto.setPlace(new PlaceDto());
        }
        if (dto.getRole() == null) {
            dto.setRole(new RoleDto());
        }
        model.addAttribute("form", dto);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/edit";
    }
    
    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", createEmptyDto());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("places", placeService.findAll());
        return getBasePath() + "/create";
    }

    @GetMapping("/find_user")
    public String searchUser(@RequestParam String searchWord, Model model) {
        List<User> users = baseService.searchUser(searchWord);
        model.addAttribute("users", users);
        return getBasePath() + "/find_user";
    }

}