package com.engsoft.sgamp.controller;

import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.service.AuthService;
import com.engsoft.sgamp.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                             @RequestParam String password,
                             Model model) {
        try {
            Usuario usuario = usuarioService.buscarPorUsername(username);
            if (usuario != null && authService.authenticate(username, password)) {
                return "redirect:/painel";
            }
            model.addAttribute("error", "Credenciais inválidas");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao realizar login");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
