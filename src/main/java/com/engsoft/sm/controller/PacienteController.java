package com.engsoft.sm.controller;

import com.engsoft.sm.dto.ConsultaDTO;
import com.engsoft.sm.dto.PacienteDTO;
import com.engsoft.sm.dto.PacienteFiltroDTO;
import com.engsoft.sm.entity.Consulta;
import com.engsoft.sm.entity.Medico;
import com.engsoft.sm.entity.Paciente;
import com.engsoft.sm.exception.ResourceNotFoundException;
import com.engsoft.sm.repository.MedicoRepository;
import com.engsoft.sm.repository.PacienteRepository;
import com.engsoft.sm.service.ConsultaService;
import com.engsoft.sm.service.PacienteService;
import jakarta.validation.Valid;


import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteService pacienteService;
    private final ConsultaService consultaService;

    
    public PacienteController(PacienteRepository pacienteRepository,
                              MedicoRepository medicoRepository,
                              PacienteService pacienteService,
                              ConsultaService consultaService) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
    }





    @GetMapping("/novo")
        public String exibirFormularioCadastro(Model model) {
        model.addAttribute("pacienteDTO", new PacienteDTO());
        return "pacientes/cadastrar-paciente"; // nome do template
    }



    
    @GetMapping("/procurar")
        public String exibirFormularioBuscaPaciente(Model model) {
        model.addAttribute("filtro", new PacienteFiltroDTO());
        return "pacientes/procurar-paciente"; // nome do template que mostra o campo de busca
    }







    /**
     * Exibe a lista de pacientes com filtros e paginação.
     */
    @GetMapping("/listar")
    public String listarPacientes(@ModelAttribute("filtro") PacienteFiltroDTO filtroDTO,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  @RequestParam(value = "sort", defaultValue = "nomeCompleto") String sort,
                                  @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                  Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<Paciente> paginaPacientes = pacienteService.listarPacientesComFiltros(filtroDTO, pageable);

        model.addAttribute("paginaPacientes", paginaPacientes);
        model.addAttribute("filtro", filtroDTO);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        if (paginaPacientes.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, paginaPacientes.getTotalPages())
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "pacientes/listar-pacientes";
    }

    /**
     * Exibe o formulário para editar um paciente.
     */
    @GetMapping("/editar/{id}")
    public String exibirFormularioEditarPaciente(@PathVariable("id") Long id, Model model) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        PacienteDTO pacienteDTO = pacienteService.converterParaDTO(paciente);

        model.addAttribute("pacienteDTO", pacienteDTO);
        model.addAttribute("editMode", true);
        return "pacientes/cadastrar-paciente";
    }

    /**
     * Processa a submissão do formulário de edição de paciente.
     */
    @PostMapping("/atualizar/{id}")
    public String atualizarPaciente(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute("pacienteDTO") PacienteDTO pacienteDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "pacientes/cadastrar-paciente";
        }

        try {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            pacienteService.atualizarPaciente(id, pacienteDTO, username);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Dados do paciente atualizados com sucesso!");
            return "redirect:/pacientes/" + id;
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/pacientes/listar";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao atualizar o paciente: " + e.getMessage());
            model.addAttribute("editMode", true);
            return "pacientes/cadastrar-paciente";
        }
    }

    /**
     * Exibe a tela de detalhes de um paciente com seu histórico de consultas.
     */
    @GetMapping("/{id}")
    public String exibirTelaPaciente(@PathVariable("id") Long id, Model model) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        List<Consulta> consultas = consultaService.buscarConsultasPorPacienteId(id);

        model.addAttribute("paciente", paciente);
        model.addAttribute("consultas", consultas);
        model.addAttribute("novaConsultaDTO", new ConsultaDTO());

        return "pacientes/tela-paciente";
    }

    /**
     * Atualiza os dados de um paciente (caso usado fora do controller também).
     */
    @Transactional
    public Paciente atualizarPacienteInterno(Long id, PacienteDTO pacienteDTO, String medicoUsername) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));
        Medico medico = medicoRepository.findByUsername(medicoUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoUsername));

        paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setNif(pacienteDTO.getNif());
        paciente.setDocumentoIdentidade(pacienteDTO.getDocumentoIdentidade());
        paciente.setEnderecoCompleto(pacienteDTO.getEnderecoCompleto());
        paciente.setContactosTelefonicos(pacienteDTO.getContactosTelefonicos());
        paciente.setEstadoCivil(pacienteDTO.getEstadoCivil());
        paciente.setProfissao(pacienteDTO.getProfissao());
        paciente.setNumeroUtenteSNS(pacienteDTO.getNumeroUtenteSNS());
        paciente.setNomeFamiliarResponsavel(pacienteDTO.getNomeFamiliarResponsavel());
        paciente.setContactoFamiliarResponsavel(pacienteDTO.getContactoFamiliarResponsavel());
        paciente.setSexo(pacienteDTO.getSexo());
        paciente.setAtualizadoPorMedico(medico);

        return pacienteRepository.save(paciente);
    }
}
