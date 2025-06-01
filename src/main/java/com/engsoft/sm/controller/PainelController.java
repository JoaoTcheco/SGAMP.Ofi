package com.engsoft.sm.controller;

import org.springframework.security.core.Authentication; // Para obter informações do usuário logado
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para o painel principal da aplicação.
 */
@Controller
public class PainelController {

    
    @GetMapping("/painel")
    public String painelPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("nomeUsuarioLogado", userDetails.getUsername());
            // Você poderia buscar o nome completo do médico se quisesse exibir:
            // Medico medico = medicoRepository.findByUsername(userDetails.getUsername()).orElse(null);
            // if (medico != null) model.addAttribute("nomeCompletoMedico", medico.getNomeCompleto());
        }
        return "painel"; // Retorna painel.html
    }
}