package com.engsoft.sgamp.controller;

import com.engsoft.sgamp.DTO.LoginDTO;
import com.engsoft.sgamp.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("loginDTO")) {
            model.addAttribute("loginDTO", new LoginDTO());
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginDTO loginDTO, 
                             RedirectAttributes redirectAttributes) {
        if (authService.authenticate(loginDTO)) {
            return "redirect:/painel";
        }
        
        redirectAttributes.addFlashAttribute("error", "Usuário ou senha inválidos");
        redirectAttributes.addFlashAttribute("loginDTO", loginDTO);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/login?logout";
    }
}
