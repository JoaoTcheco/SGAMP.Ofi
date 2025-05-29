package com.engsoft.sgamp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelController {

    @GetMapping("/painel")
    public String showPainel() {
        return "painel";
    }
}