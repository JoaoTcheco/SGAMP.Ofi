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

    /**
     * Exibe a página de login.
     * Também trata os parâmetros 'error' e 'logout' que podem ser enviados
     * pelo Spring Security após uma tentativa de login falha ou um logout bem-sucedido.
     *
     * @param model O Model para adicionar atributos para a view.
     * @param error Parâmetro opcional indicando erro no login.
     * @param logout Parâmetro opcional indicando logout bem-sucedido.
     * @return O nome da view de login ("login").
     */
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
