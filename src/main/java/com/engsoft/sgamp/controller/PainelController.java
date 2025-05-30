

package com.engsoft.sgamp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelController {

    @GetMapping("/painel")
    public String painel(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "painel";
    }
}