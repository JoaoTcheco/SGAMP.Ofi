package com.engsoft.sm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller para lidar com autenticação (login/logout).
 */
@Controller
public class AuthController {

   
    @GetMapping("/login")
    public String loginPage(Model model,
                          @RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("errorMessage", "Usuário ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você foi desconectado com sucesso!");
        }
        return "login"; // Retorna o nome do arquivo HTML (login.html)
    }

    // O processamento do POST para /login é feito automaticamente pelo Spring Security.
    // O processamento do /logout também é feito pelo Spring Security.
}
