package com.engsoft.sgamp.controller;

import com.engsoft.sgamp.DTO.LoginDTO;
import com.engsoft.sgamp.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping
    public String login(LoginDTO loginDTO, Model model) {
        if (authService.authenticate(loginDTO)) {
            return "redirect:/painel";
        }
        model.addAttribute("error", "Credenciais inválidas");
        return "login";
    }
}