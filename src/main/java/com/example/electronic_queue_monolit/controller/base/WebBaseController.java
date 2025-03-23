package com.example.electronic_queue_monolit.controller.base;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;


public abstract class WebBaseController {

    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("currentUser", authentication.getPrincipal());
        }
    }
} 