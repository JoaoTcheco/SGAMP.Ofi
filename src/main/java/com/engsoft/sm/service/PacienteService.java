package com.engsoft.sm.service;

import com.engsoft.sm.dto.PacienteDTO;
import com.engsoft.sm.dto.PacienteFiltroDTO; // Usaremos para filtros
import com.engsoft.sm.entity.Consulta; // Import necessário
import com.engsoft.sm.entity.Medico;
import com.engsoft.sm.entity.Paciente;
import com.engsoft.sm.repository.MedicoRepository; // Para buscar o médico logado se necessário
import com.engsoft.sm.repository.PacienteRepository;
import com.engsoft.sm.exception.ResourceNotFoundException; // Exceção customizada

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // Para paginação
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Para informações de paginação
import org.springframework.data.jpa.domain.Specification; // Para queries dinâmicas
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para gerenciamento de transações

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate; // Para construir predicates na Specification

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar a lógica de negócios relacionada a Pacientes.
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    @Autowired // Boa prática adicionar @Autowired no construtor para injeção
    public PacienteService(PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

  
    public PacienteDTO converterParaDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId()); // Importante para edição
        dto.setNomeCompleto(paciente.getNomeCompleto());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setNuit(paciente.getNuit());
        dto.setDocumentoIdentidade(paciente.getDocumentoIdentidade());
        dto.setEnderecoCompleto(paciente.getEnderecoCompleto());
        dto.setContactosTelefonicos(paciente.getContactosTelefonicos());
        dto.setEstadoCivil(paciente.getEstadoCivil());
        dto.setProfissao(paciente.getProfissao());
        dto.setNumeroPacienteNP(paciente.getNumeroPacienteNP());
        dto.setNomeFamiliarResponsavel(paciente.getNomeFamiliarResponsavel());
        dto.setContactoFamiliarResponsavel(paciente.getContactoFamiliarResponsavel());
        dto.setSexo(paciente.getSexo());
        return dto;
    }

   
    @Transactional
    public Paciente criarPaciente(PacienteDTO pacienteDTO, String medicoLogadoUsername) {
        Medico medicoCriador = medicoRepository.findByUsername(medicoLogadoUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoLogadoUsername));

        Paciente paciente = new Paciente();
        // Mapear dados do DTO para a entidade
        paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());

        // <<< AQUI É A ADIÇÃO NECESSÁRIA PARA NUIT >>>
        if (pacienteDTO.getNuit() != null && pacienteDTO.getNuit().trim().isEmpty()) {
            paciente.setNuit(null);
        } else {
            paciente.setNuit(pacienteDTO.getNuit());
        }

        paciente.setDocumentoIdentidade(pacienteDTO.getDocumentoIdentidade());
        paciente.setEnderecoCompleto(pacienteDTO.getEnderecoCompleto());
        paciente.setContactosTelefonicos(pacienteDTO.getContactosTelefonicos());
        paciente.setEstadoCivil(pacienteDTO.getEstadoCivil());
        paciente.setProfissao(pacienteDTO.getProfissao());

        // <<< AQUI É A ADIÇÃO NECESSÁRIA PARA NUMERO_PACIENTE_NP >>>
        if (pacienteDTO.getNumeroPacienteNP() != null && pacienteDTO.getNumeroPacienteNP().trim().isEmpty()) {
            paciente.setNumeroPacienteNP(null);
        } else {
            paciente.setNumeroPacienteNP(pacienteDTO.getNumeroPacienteNP());
        }

        paciente.setNomeFamiliarResponsavel(pacienteDTO.getNomeFamiliarResponsavel());
        paciente.setContactoFamiliarResponsavel(pacienteDTO.getContactoFamiliarResponsavel());
        paciente.setSexo(pacienteDTO.getSexo());

        paciente.setCriadoPorMedico(medicoCriador);
        // dataCadastro é definida pelo @PrePersist na entidade Paciente

        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente atualizarPaciente(Long id, PacienteDTO pacienteDTO, String medicoLogadoUsername) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));

        Medico medicoAtualizador = medicoRepository.findByUsername(medicoLogadoUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoLogadoUsername));

        // Mapear dados do DTO para a entidade existente
        pacienteExistente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        pacienteExistente.setDataNascimento(pacienteDTO.getDataNascimento());

        // <<< AQUI É A ADIÇÃO NECESSÁRIA PARA NUIT >>>
        if (pacienteDTO.getNuit() != null && pacienteDTO.getNuit().trim().isEmpty()) {
            pacienteExistente.setNuit(null);
        } else {
            pacienteExistente.setNuit(pacienteDTO.getNuit());
        }

        pacienteExistente.setDocumentoIdentidade(pacienteDTO.getDocumentoIdentidade());
        pacienteExistente.setEnderecoCompleto(pacienteDTO.getEnderecoCompleto());
        pacienteExistente.setContactosTelefonicos(pacienteDTO.getContactosTelefonicos());
        pacienteExistente.setEstadoCivil(pacienteDTO.getEstadoCivil());
        pacienteExistente.setProfissao(pacienteDTO.getProfissao());

        // <<< AQUI É A ADIÇÃO NECESSÁRIA PARA NUMERO_PACIENTE_NP >>>
        if (pacienteDTO.getNumeroPacienteNP() != null && pacienteDTO.getNumeroPacienteNP().trim().isEmpty()) {
            pacienteExistente.setNumeroPacienteNP(null);
        } else {
            pacienteExistente.setNumeroPacienteNP(pacienteDTO.getNumeroPacienteNP());
        }

        pacienteExistente.setNomeFamiliarResponsavel(pacienteDTO.getNomeFamiliarResponsavel());
        pacienteExistente.setContactoFamiliarResponsavel(pacienteDTO.getContactoFamiliarResponsavel());
        pacienteExistente.setSexo(pacienteDTO.getSexo());

        pacienteExistente.setAtualizadoPorMedico(medicoAtualizador);
        // dataUltimaAtualizacao é gerenciada por @PreUpdate na entidade Paciente

        return pacienteRepository.save(pacienteExistente);
    }

    /**
     * Busca um paciente pelo seu ID.
     * @param id O ID do paciente.
     * @return A entidade Paciente.
     * @throws ResourceNotFoundException se o paciente não for encontrado.
     */
    @Transactional(readOnly = true)
    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));
    }

    /**
     * Busca pacientes cujo nome completo contenha o termo fornecido.
     * @param nome Termo de busca para o nome.
     * @return Lista de pacientes encontrados.
     */
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientesPorNomeContendo(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return new ArrayList<>(); // Retorna lista vazia se o nome for nulo ou vazio
        }
        return pacienteRepository.findByNomeCompletoContainingIgnoreCase(nome);
    }

    /**
     * Sugere nomes de pacientes com base em um termo parcial.
     * @param termo Termo parcial para busca de nomes.
     * @return Lista de nomes completos de pacientes que correspondem ao termo (limitado).
     */
    @Transactional(readOnly = true)
    public List<String> sugerirNomesPacientes(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        // PageRequest para limitar o número de resultados, por exemplo, os 10 primeiros.
        Pageable limit = PageRequest.of(0, 10);
        List<Paciente> pacientes = pacienteRepository.findByNomeCompletoContainingIgnoreCase(termo, limit);
        return pacientes.stream()
                .map(Paciente::getNomeCompleto)
                .collect(Collectors.toList());
    }


    /**
     * Lista pacientes com base em filtros e paginação.
     * @param filtro DTO com os critérios de filtro.
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação).
     * @return Uma Página (Page) de pacientes que correspondem aos filtros.
     */
    @Transactional(readOnly = true)
    public Page<Paciente> listarPacientesComFiltros(PacienteFiltroDTO filtro, Pageable pageable) {
        Specification<Paciente> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nome
            if (filtro.getNome() != null && !filtro.getNome().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeCompleto")),
                        "%" + filtro.getNome().toLowerCase() + "%"));
            }

            // Filtro por sexo
            if (filtro.getSexo() != null && !filtro.getSexo().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sexo"), filtro.getSexo()));
            }

            // Filtro por idade mínima e máxima
            LocalDate hoje = LocalDate.now();
            if (filtro.getIdadeMinima() != null) {
                LocalDate dataNascimentoMaxima = hoje.minusYears(filtro.getIdadeMinima());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataNascimento"), dataNascimentoMaxima));
            }
            if (filtro.getIdadeMaxima() != null) {
                LocalDate dataNascimentoMinima = hoje.minusYears(filtro.getIdadeMaxima() + 1).plusDays(1);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataNascimento"), dataNascimentoMinima));
            }

            // Filtro por sintoma
            if (filtro.getSintoma() != null && !filtro.getSintoma().trim().isEmpty()) {
                Join<Paciente, Consulta> consultaJoin = root.join("consultas", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(consultaJoin.get("sintomasAtuais")),
                        "%" + filtro.getSintoma().toLowerCase() + "%"));
                query.distinct(true); // Evitar duplicatas se múltiplas consultas tiverem o sintoma
            }

            // Filtro por doença (informacoesSituacaoClinica)
            if (filtro.getDoenca() != null && !filtro.getDoenca().trim().isEmpty()) {
                Join<Paciente, Consulta> consultaJoin = root.join("consultas", JoinType.LEFT); // Reutiliza ou cria novo join
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(consultaJoin.get("informacoesSituacaoClinica")),
                        "%" + filtro.getDoenca().toLowerCase() + "%"));
                query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return pacienteRepository.findAll(spec, pageable);
    }

    /**
     * Retorna todos os pacientes (usar com cautela, preferir paginação).
     * @return Lista de todos os pacientes.
     */
    @Transactional(readOnly = true)
    public List<Paciente> listarTodosPacientes() {
        return pacienteRepository.findAll();
    }

    // O método atualizarPacienteInterno pode ser removido, pois você já tem atualizarPaciente acima que é chamado pelo Controller
    // Se você não o usa em mais nenhum lugar, ele é redundante.
    @Transactional
    public Paciente atualizarPacienteInterno(Long id, PacienteDTO pacienteDTO, String medicoUsername) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));
        Medico medico = medicoRepository.findByUsername(medicoUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoUsername));

        // Mapeamento do DTO para a Entidade
        paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());

        // LÓGICA PARA NUIT (ADICIONAR/MODIFICAR ISSO AQUI TAMBÉM SE VOCÊ FOR MANTER ESTE MÉTODO INTERNO)
        if (pacienteDTO.getNuit() != null && pacienteDTO.getNuit().trim().isEmpty()) {
            paciente.setNuit(null);
        } else {
            paciente.setNuit(pacienteDTO.getNuit());
        }

        paciente.setDocumentoIdentidade(pacienteDTO.getDocumentoIdentidade());
        paciente.setEnderecoCompleto(pacienteDTO.getEnderecoCompleto());
        paciente.setContactosTelefonicos(pacienteDTO.getContactosTelefonicos());
        paciente.setEstadoCivil(pacienteDTO.getEstadoCivil());
        paciente.setProfissao(pacienteDTO.getProfissao());

        // LÓGICA PARA NUMERO_PACIENTE_NP (ADICIONAR/MODIFICAR ISSO AQUI TAMBÉM SE VOCÊ FOR MANTER ESTE MÉTODO INTERNO)
        if (pacienteDTO.getNumeroPacienteNP() != null && pacienteDTO.getNumeroPacienteNP().trim().isEmpty()) {
            paciente.setNumeroPacienteNP(null);
        } else {
            paciente.setNumeroPacienteNP(pacienteDTO.getNumeroPacienteNP());
        }

        paciente.setNomeFamiliarResponsavel(pacienteDTO.getNomeFamiliarResponsavel());
        paciente.setContactoFamiliarResponsavel(pacienteDTO.getContactoFamiliarResponsavel());
        paciente.setSexo(pacienteDTO.getSexo());
        paciente.setAtualizadoPorMedico(medico);

        return pacienteRepository.save(paciente);
    }
}