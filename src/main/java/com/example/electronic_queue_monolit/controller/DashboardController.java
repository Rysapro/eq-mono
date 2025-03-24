package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.service.UserService;
import com.example.electronic_queue_monolit.controller.base.WebBaseController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController extends WebBaseController {
    
    private final UserService userService;
    
    public DashboardController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();
            model.addAttribute("currentUser", currentUser);
            
            model.addAttribute("totalTickets", 19);
            model.addAttribute("activeTickets", 0);
            model.addAttribute("totalUsers", 3);
            model.addAttribute("totalPlaces", 1);
            
            return "dashboard";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/admin-overview";
    }
} 