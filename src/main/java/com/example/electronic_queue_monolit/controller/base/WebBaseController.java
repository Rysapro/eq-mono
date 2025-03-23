package com.example.electronic_queue_monolit.controller.base;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Базовый контроллер для веб-страниц, который добавляет информацию о пользователе в модель
 */
public abstract class WebBaseController {
    
    /**
     * Добавляет текущего пользователя в модель
     * Этот метод будет вызываться перед каждым методом контроллера с аннотацией @RequestMapping
     * 
     * @param model модель для страницы
     * @param authentication объект аутентификации
     */
    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("currentUser", authentication.getPrincipal());
        }
    }
} 