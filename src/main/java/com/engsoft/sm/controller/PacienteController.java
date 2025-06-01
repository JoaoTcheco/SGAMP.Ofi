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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
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

    /**
     * Exibe o formulário de cadastro de um novo paciente.
     * @param model O Model para adicionar atributos para a view.
     * @return O nome da view de cadastro ("pacientes/cadastrar-paciente").
     */
    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("pacienteDTO", new PacienteDTO());
        model.addAttribute("editMode", false); // Indica que não é modo de edição
        return "pacientes/cadastrar-paciente";
    }

    /**
     * Processa a submissão do formulário de cadastro de novo paciente.
     * @param pacienteDTO O DTO com os dados do novo paciente, validado.
     * @param bindingResult Contém os resultados da validação do DTO.
     * @param authentication Objeto de autenticação para obter o médico logado.
     * @param redirectAttributes Para adicionar atributos flash (disponíveis após redirect).
     * @param model O Model para adicionar atributos de volta para a view em caso de erro.
     * @return Redireciona para o painel em caso de sucesso, ou retorna para o formulário em caso de erro.
     */
    @PostMapping("/salvar")
    public String salvarNovoPaciente(@Valid @ModelAttribute("pacienteDTO") PacienteDTO pacienteDTO,
                                     BindingResult bindingResult,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            return "pacientes/cadastrar-paciente";
        }

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String usernameMedicoLogado = userDetails.getUsername();
            pacienteService.criarPaciente(pacienteDTO, usernameMedicoLogado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Paciente cadastrado com sucesso!");
            return "redirect:/painel?pacienteCriado=true"; // Redireciona para o painel
        } catch (Exception e) {
            // Logar o erro: logger.error("Erro ao salvar novo paciente", e);
            model.addAttribute("mensagemErro", "Erro ao salvar o paciente: " + e.getMessage());
            model.addAttribute("editMode", false);
            return "pacientes/cadastrar-paciente";
        }
    }


    /**
     * Exibe o formulário de busca de paciente.
     * @param model O Model para adicionar atributos para a view.
     * @return O nome da view de procurar paciente ("pacientes/procurar-paciente").
     */
    @GetMapping("/procurar")
    public String exibirFormularioBuscaPaciente(Model model, @RequestParam(value = "nome", required = false) String nome) {
        model.addAttribute("termoBusca", nome);
        if (nome != null && !nome.trim().isEmpty()) {
            List<Paciente> pacientesEncontrados = pacienteService.buscarPacientesPorNomeContendo(nome);
            model.addAttribute("pacientesEncontrados", pacientesEncontrados);
            if (pacientesEncontrados.isEmpty()) {
                model.addAttribute("mensagemInfo", "Nenhum paciente encontrado com o nome: " + nome);
            }
        } else {
            model.addAttribute("pacientesEncontrados", Collections.emptyList());
        }
        return "pacientes/procurar-paciente";
    }

    /**
     * Endpoint API para fornecer sugestões de nomes de pacientes para autocompletar.
     * @param termo O termo de busca parcial para nomes de pacientes.
     * @return ResponseEntity contendo uma lista de nomes de pacientes que correspondem ao termo.
     */
    @GetMapping("/api/sugestoes-nomes")
    @ResponseBody // Indica que o retorno será o corpo da resposta (JSON)
    public ResponseEntity<List<String>> sugerirNomesPacientes(@RequestParam("termo") String termo) {
        if (termo == null || termo.trim().length() < 2) { // Evita buscas com termos muito curtos
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<String> nomesSugeridos = pacienteService.sugerirNomesPacientes(termo);
        return ResponseEntity.ok(nomesSugeridos);
    }

    /**
     * Processa a busca de pacientes pelo nome e exibe os resultados.
     * Este método é chamado quando o formulário de "procurar-paciente" é submetido.
     * @param nome O nome (ou parte do nome) do paciente a ser buscado.
     * @param model O Model para adicionar atributos para a view.
     * @return O nome da view de procurar paciente com os resultados ("pacientes/procurar-paciente").
     */
    @GetMapping("/resultados-busca")
    public String buscarPacientes(@RequestParam("nome") String nome, Model model) {
        model.addAttribute("termoBusca", nome);
        if (nome == null || nome.trim().isEmpty()) {
            model.addAttribute("mensagemInfo", "Por favor, insira um nome para buscar.");
            model.addAttribute("pacientesEncontrados", Collections.emptyList());
        } else {
            List<Paciente> pacientesEncontrados = pacienteService.buscarPacientesPorNomeContendo(nome);
            model.addAttribute("pacientesEncontrados", pacientesEncontrados);
            if (pacientesEncontrados.isEmpty()) {
                model.addAttribute("mensagemInfo", "Nenhum paciente encontrado com o nome: " + nome);
            }
        }
        return "pacientes/procurar-paciente"; // Reutiliza a mesma view para mostrar os resultados
    }


    /**
     * Exibe a lista de pacientes com filtros e paginação.
     * @param filtroDTO DTO com os critérios de filtro.
     * @param page Número da página atual (padrão 0).
     * @param size Tamanho da página (padrão 10).
     * @param sort Campo para ordenação (padrão "nomeCompleto").
     * @param direction Direção da ordenação (padrão "ASC").
     * @param model O Model para adicionar atributos para a view.
     * @return O nome da view de listagem de pacientes ("pacientes/listar-pacientes").
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
        model.addAttribute("filtro", filtroDTO); // Mantém o filtro na view
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        if (paginaPacientes.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, paginaPacientes.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "pacientes/listar-pacientes";
    }


    /**
     * Exibe o formulário para editar um paciente existente.
     * @param id O ID do paciente a ser editado.
     * @param model O Model para adicionar atributos para a view.
     * @param redirectAttributes Para mensagens em caso de não encontrar o paciente.
     * @return O nome da view de cadastro/edição ("pacientes/cadastrar-paciente") ou redireciona se não encontrar.
     */
    @GetMapping("/editar/{id}")
    public String exibirFormularioEditarPaciente(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Paciente paciente = pacienteService.buscarPacientePorId(id);
            PacienteDTO pacienteDTO = pacienteService.converterParaDTO(paciente); // Usa o método do service
            pacienteDTO.setId(id); // Garante que o ID está no DTO para o formulário

            model.addAttribute("pacienteDTO", pacienteDTO);
            model.addAttribute("editMode", true); // Indica que é modo de edição
            return "pacientes/cadastrar-paciente";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Paciente não encontrado com ID: " + id);
            return "redirect:/pacientes/listar";
        }
    }


    /**
     * Processa a submissão do formulário de edição de paciente.
     * @param id O ID do paciente que está sendo atualizado.
     * @param pacienteDTO O DTO com os dados atualizados do paciente, validado.
     * @param bindingResult Contém os resultados da validação do DTO.
     * @param model O Model para adicionar atributos de volta para a view em caso de erro de validação.
     * @param authentication Objeto de autenticação para obter o médico logado.
     * @param redirectAttributes Para adicionar atributos flash (disponíveis após redirect).
     * @return Redireciona para a tela de detalhes do paciente em caso de sucesso, ou retorna para o formulário de edição em caso de erro.
     */
    @PostMapping("/atualizar/{id}")
    public String atualizarPaciente(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute("pacienteDTO") PacienteDTO pacienteDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true); // Mantém em modo de edição
            pacienteDTO.setId(id); // Garante que o ID está no DTO para o formulário
            model.addAttribute("pacienteDTO", pacienteDTO); // Devolve o DTO com erros
            return "pacientes/cadastrar-paciente";
        }

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String usernameMedicoLogado = userDetails.getUsername();
            pacienteService.atualizarPaciente(id, pacienteDTO, usernameMedicoLogado);
            redirectAttributes.addFlashAttribute("mensagemGlobalSucesso", "Dados do paciente atualizados com sucesso!");
            return "redirect:/pacientes/" + id; // Redireciona para a tela de detalhes do paciente
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/pacientes/listar";
        } catch (Exception e) {
            // Logar o erro: logger.error("Erro ao atualizar paciente ID " + id, e);
            model.addAttribute("mensagemErro", "Erro ao atualizar o paciente: " + e.getMessage());
            model.addAttribute("editMode", true);
            pacienteDTO.setId(id);
            model.addAttribute("pacienteDTO", pacienteDTO);
            return "pacientes/cadastrar-paciente";
        }
    }


    /**
     * Exibe a tela de detalhes de um paciente com seu histórico de consultas.
     * @param id O ID do paciente cujos detalhes serão exibidos.
     * @param model O Model para adicionar atributos para a view.
     * @param redirectAttributes Para mensagens em caso de não encontrar o paciente.
     * @return O nome da view de detalhes do paciente ("pacientes/tela-paciente") ou redireciona se não encontrar.
     */
    @GetMapping("/{id}")
    public String exibirTelaPaciente(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Paciente paciente = pacienteService.buscarPacientePorId(id);
            List<Consulta> consultas = consultaService.buscarConsultasPorPacienteId(id);

            model.addAttribute("paciente", paciente);
            model.addAttribute("consultas", consultas);
            model.addAttribute("novaConsultaDTO", new ConsultaDTO()); // Para o formulário de nova consulta

            // Adiciona mensagens flash ao modelo se existirem
            if (redirectAttributes.getFlashAttributes().containsKey("mensagemGlobalSucesso")) {
                model.addAttribute("mensagemGlobalSucesso", redirectAttributes.getFlashAttributes().get("mensagemGlobalSucesso"));
            }
            if (redirectAttributes.getFlashAttributes().containsKey("mensagemGlobalErro")) {
                model.addAttribute("mensagemGlobalErro", redirectAttributes.getFlashAttributes().get("mensagemGlobalErro"));
            }


            return "pacientes/tela-paciente";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Paciente não encontrado com ID: " + id);
            return "redirect:/pacientes/listar";
        }
    }


    /**
     * Método interno para atualizar paciente, pode ser removido se não for usado em outros contextos.
     * O @Transactional aqui pode ser redundante se o método de serviço já for transacional.
     */
    @Transactional
    public Paciente atualizarPacienteInterno(Long id, PacienteDTO pacienteDTO, String medicoUsername) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));
        Medico medico = medicoRepository.findByUsername(medicoUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoUsername));

        // Mapeamento do DTO para a Entidade
        paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setNuit(pacienteDTO.getNuit());
        paciente.setDocumentoIdentidade(pacienteDTO.getDocumentoIdentidade());
        paciente.setEnderecoCompleto(pacienteDTO.getEnderecoCompleto());
        paciente.setContactosTelefonicos(pacienteDTO.getContactosTelefonicos());
        paciente.setEstadoCivil(pacienteDTO.getEstadoCivil());
        paciente.setProfissao(pacienteDTO.getProfissao());
        paciente.setNumeroPacienteNP(pacienteDTO.getNumeroPacienteNP());
        paciente.setNomeFamiliarResponsavel(pacienteDTO.getNomeFamiliarResponsavel());
        paciente.setContactoFamiliarResponsavel(pacienteDTO.getContactoFamiliarResponsavel());
        paciente.setSexo(pacienteDTO.getSexo());
        paciente.setAtualizadoPorMedico(medico);
        // dataUltimaAtualizacao é gerenciada por @PreUpdate na entidade Paciente

        return pacienteRepository.save(paciente);
    }
}