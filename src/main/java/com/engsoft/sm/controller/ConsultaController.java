package com.engsoft.sm.controller;

import com.engsoft.sm.dto.ConsultaDTO;
import com.engsoft.sm.entity.Paciente; // Para recarregar dados do paciente em caso de erro
import com.engsoft.sm.entity.Consulta; // Para a lista de consultas
import com.engsoft.sm.service.ConsultaService;
import com.engsoft.sm.service.PacienteService; // Para buscar paciente em caso de erro na validação

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List; // Para a lista de consultas

/**
 * Controller para gerenciar operações relacionadas a Consultas Médicas.
 */
@Controller
@RequestMapping("/consultas") // Todas as URLs deste controller começarão com /consultas
public class ConsultaController {

    private final ConsultaService consultaService;
    private final PacienteService pacienteService; // Para recarregar dados do paciente em caso de erro

    
    public ConsultaController(ConsultaService consultaService, PacienteService pacienteService) {
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
    }

    /**
     * Processa a submissão do formulário de nova consulta para um paciente específico.
     *
     * @param pacienteId O ID do paciente ao qual a consulta pertence.
     * @param consultaDTO O DTO com os dados da nova consulta, validado.
     * @param bindingResult Contém os resultados da validação do DTO.
     * @param authentication Objeto de autenticação para obter o médico logado.
     * @param redirectAttributes Para adicionar atributos flash (disponíveis após redirect).
     * @param model O Model para adicionar atributos de volta para a view em caso de erro.
     * @return Redireciona para o painel em caso de sucesso, ou retorna para a tela do paciente em caso de erro.
     */
    @PostMapping("/paciente/{pacienteId}/salvar") // Mapeia para POST /consultas/paciente/{id_do_paciente}/salvar
    public String salvarNovaConsulta(@PathVariable("pacienteId") Long pacienteId,
                                     @Valid @ModelAttribute("novaConsultaDTO") ConsultaDTO consultaDTO,
                                     BindingResult bindingResult,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String usernameMedicoLogado = userDetails.getUsername();

        // Verifica se há erros de validação no DTO da consulta
        if (bindingResult.hasErrors()) {
            // Se houver erros, precisamos recarregar os dados do paciente e as consultas existentes
            // para reexibir a página 'tela-paciente' corretamente com as mensagens de erro.
            Paciente paciente = pacienteService.buscarPacientePorId(pacienteId);
            List<Consulta> consultasAnteriores = consultaService.buscarConsultasPorPacienteId(pacienteId);

            model.addAttribute("paciente", paciente);
            model.addAttribute("consultas", consultasAnteriores);
            // O 'novaConsultaDTO' com os erros já está no model por causa do @ModelAttribute
            model.addAttribute("mensagemGlobalErro", "Erro na validação dos dados da consulta. Por favor, verifique os campos.");
            return "pacientes/tela-paciente"; // Retorna para a tela do paciente com os erros
        }

        // Tenta salvar a nova consulta
        try {
            consultaService.criarConsulta(pacienteId, consultaDTO, usernameMedicoLogado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Nova consulta registrada com sucesso!");
            // Conforme a descrição do fluxo: "Apos salvar essa consulta ele é redirecionado a tela painel."
            return "redirect:/painel?consultaCriada=true";
            // Alternativamente, poderia redirecionar de volta para a tela do paciente:
            // return "redirect:/pacientes/" + pacienteId + "?consultaCriada=true";
        } catch (Exception e) {
            // Em caso de outros erros
            redirectAttributes.addFlashAttribute("mensagemGlobalErro", "Erro ao salvar a nova consulta: " + e.getMessage());
            // Logar o erro: logger.error("Erro ao salvar consulta para paciente ID " + pacienteId, e);

            // Se quisermos voltar para a tela do paciente em caso de erro geral (além da validação)
            // precisamos carregar os dados do paciente novamente ou garantir que eles estejam disponíveis.
            // Uma abordagem mais simples para erro geral pode ser redirecionar para uma página de erro
            // ou de volta ao painel com uma mensagem de erro.
            // Por ora, vamos redirecionar para a tela do paciente.
            // Para isso, o ideal seria que a mensagem de erro fosse um flash attribute
            // e o GET de /pacientes/{id} pudesse exibi-la.
            return "redirect:/pacientes/" + pacienteId + "?erroSalvarConsulta=true";
        }
    }

   
}