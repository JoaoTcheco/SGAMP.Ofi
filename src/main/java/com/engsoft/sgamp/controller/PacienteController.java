package com.engsoft.sgamp.controller;

import com.engsoft.sgamp.model.Paciente;
import com.engsoft.sgamp.model.Paciente.Sexo;
import com.engsoft.sgamp.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/novo")
    public String showCadastroForm(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("sexos", Sexo.values());
        return "cadastro-paciente";
    }

    @PostMapping("/salvar")
    public String salvarPaciente(@ModelAttribute Paciente paciente) {
        pacienteService.salvarPaciente(paciente);
        return "redirect:/pacientes/procurar";
    }

    @GetMapping("/procurar")
    public String showProcurarForm(@RequestParam(required = false) String nome, Model model) {
        if (nome != null && !nome.isEmpty()) {
            List<Paciente> pacientes = pacienteService.buscarPorNome(nome);
            model.addAttribute("pacientes", pacientes);
        }
        return "procurar-paciente";
    }

    @GetMapping("/{id}")
    public String showPacienteDetails(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id);
        model.addAttribute("paciente", paciente);
        return "paciente-detalhes";
    }

    @GetMapping("/listar")
    public String listarPacientes(@RequestParam(required = false) String nome,
                                 @RequestParam(required = false) Integer idade,
                                 @RequestParam(required = false) Sexo sexo,
                                 Model model) {
        List<Paciente> pacientes = pacienteService.listarPorFiltros(nome, idade, sexo);
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("sexos", Sexo.values());
        return "listar-pacientes";
    }
}