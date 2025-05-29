package com.engsoft.sgamp.controller;

import com.engsoft.sgamp.model.Consulta;
import com.engsoft.sgamp.model.Paciente;
import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.service.ConsultaService;
import com.engsoft.sgamp.service.PacienteService;
import com.engsoft.sgamp.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final PacienteService pacienteService;
    private final UsuarioService usuarioService;

    public ConsultaController(ConsultaService consultaService, 
                            PacienteService pacienteService,
                            UsuarioService usuarioService) {
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nova/{pacienteId}")
    public String showNovaConsultaForm(@PathVariable Long pacienteId, Model model) {
        Paciente paciente = pacienteService.buscarPorId(pacienteId);
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setDataConsulta(LocalDate.now());
        
        model.addAttribute("consulta", consulta);
        return "nova-consulta";
    }

    @PostMapping("/salvar")
    public String salvarConsulta(@ModelAttribute Consulta consulta) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario medico = usuarioService.buscarPorUsername(username);
        
        consultaService.salvarConsulta(consulta, medico);
        return "redirect:/pacientes/" + consulta.getPaciente().getId();
    }

    @GetMapping("/editar/{id}")
    public String showEditarConsultaForm(@PathVariable Long id, Model model) {
        Consulta consulta = consultaService.buscarPorId(id);
        model.addAttribute("consulta", consulta);
        return "editar-consulta";
    }
}