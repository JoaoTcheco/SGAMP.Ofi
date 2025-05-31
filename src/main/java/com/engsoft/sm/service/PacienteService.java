package com.engsoft.sm.service;

import com.engsoft.sm.dto.PacienteDTO;
import com.engsoft.sm.dto.PacienteFiltroDTO; // Usaremos para filtros
import com.engsoft.sm.entity.Consulta;
import com.engsoft.sm.entity.Medico;
import com.engsoft.sm.entity.Paciente;
import com.engsoft.sm.repository.MedicoRepository; // Para buscar o médico logado se necessário
import com.engsoft.sm.repository.PacienteRepository;
import com.engsoft.sm.exception.ResourceNotFoundException; // Exceção customizada


import org.springframework.data.domain.Page; // Para paginação
import org.springframework.data.domain.Pageable; // Para informações de paginação
import org.springframework.data.jpa.domain.Specification; // Para queries dinâmicas
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para gerenciamento de transações

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate; // Para construir predicates na Specification
import jakarta.persistence.criteria.SetJoin;

import java.util.ArrayList;
import java.util.List;
// Para cálculo de idade

/**
 * Serviço para gerenciar a lógica de negócios relacionada a Pacientes.
 */
@Service
public class PacienteService {



    public PacienteDTO converterParaDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setNomeCompleto(paciente.getNomeCompleto());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setNif(paciente.getNif());
        dto.setDocumentoIdentidade(paciente.getDocumentoIdentidade());
        dto.setEnderecoCompleto(paciente.getEnderecoCompleto());
        dto.setContactosTelefonicos(paciente.getContactosTelefonicos());
        dto.setEstadoCivil(paciente.getEstadoCivil());
        dto.setProfissao(paciente.getProfissao());
        dto.setNumeroUtenteSNS(paciente.getNumeroUtenteSNS());
        dto.setNomeFamiliarResponsavel(paciente.getNomeFamiliarResponsavel());
        dto.setContactoFamiliarResponsavel(paciente.getContactoFamiliarResponsavel());
        dto.setSexo(paciente.getSexo());
        return dto;
    }
    
      
    




    // Em src/main/java/com/engsoft/sm/service/PacienteService.java
// ... (importações e métodos existentes)

    /**
     * Atualiza os dados de um paciente existente.
     *
     * @param id O ID do paciente a ser atualizado.
     * @param pacienteDTO DTO com os novos dados do paciente.
     * @param medicoLogadoUsername Username do médico que está realizando a atualização.
     * @return A entidade Paciente atualizada.
     * @throws ResourceNotFoundException se o paciente ou o médico não forem encontrados.
     */
    @Transactional
    public Paciente atualizarPaciente(Long id, PacienteDTO pacienteDTO, String medicoLogadoUsername) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));

        Medico medicoAtualizador = medicoRepository.findByUsername(medicoLogadoUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoLogadoUsername));

        // Mapear dados do DTO para a entidade existente
        pacienteExistente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        pacienteExistente.setDataNascimento(pacienteDTO.getDataNascimento());
        pacienteExistente.setNif(pacienteDTO.getNif());
        pacienteExistente.setDocumentoIdentidade(pacienteDTO.getDocumentoIdentidade());
        pacienteExistente.setEnderecoCompleto(pacienteDTO.getEnderecoCompleto());
        pacienteExistente.setContactosTelefonicos(pacienteDTO.getContactosTelefonicos());
        pacienteExistente.setEstadoCivil(pacienteDTO.getEstadoCivil());
        pacienteExistente.setProfissao(pacienteDTO.getProfissao());
        pacienteExistente.setNumeroUtenteSNS(pacienteDTO.getNumeroUtenteSNS());
        pacienteExistente.setNomeFamiliarResponsavel(pacienteDTO.getNomeFamiliarResponsavel());
        pacienteExistente.setContactoFamiliarResponsavel(pacienteDTO.getContactoFamiliarResponsavel());
        pacienteExistente.setSexo(pacienteDTO.getSexo());

        // Define o médico que atualizou e a data da última atualização
        pacienteExistente.setAtualizadoPorMedico(medicoAtualizador);
        // pacienteExistente.setDataUltimaAtualizacao(LocalDateTime.now()); // Já é feito pelo @PreUpdate na entidade

        return pacienteRepository.save(pacienteExistente);
    }




    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository; // Se precisarmos carregar o médico

    
    public PacienteService(PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    /**
     * Cria um novo paciente no sistema.
     *
     * @param pacienteDTO DTO com os dados do paciente a ser criado.
     * @param medicoLogadoUsername Username do médico que está realizando o cadastro.
     * @return A entidade Paciente que foi salva.
     */
    @Transactional // Garante que a operação seja atômica
    public Paciente criarPaciente(PacienteDTO pacienteDTO, String medicoLogadoUsername) {
        // Busca o médico que está realizando o cadastro
        Medico medicoCriador = medicoRepository.findByUsername(medicoLogadoUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoLogadoUsername));

        Paciente paciente = new Paciente();
        // Mapear dados do DTO para a entidade
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

        // Define o médico que cadastrou (assinatura automática)
        paciente.setCriadoPorMedico(medicoCriador);
        // dataCadastro e dataUltimaAtualizacao são definidos pelos callbacks @PrePersist/@PreUpdate na entidade

        return pacienteRepository.save(paciente);
    }

    /**
     * Busca um paciente pelo seu ID.
     *
     * @param id O ID do paciente.
     * @return A entidade Paciente.
     * @throws ResourceNotFoundException se o paciente não for encontrado.
     */
    @Transactional(readOnly = true) // Transação apenas de leitura, otimizada
    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));
    }

    /**
     * Busca pacientes cujo nome completo contenha o termo fornecido.
     *
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
     * Lista pacientes com base em filtros e paginação.
     *
     * @param filtro DTO com os critérios de filtro.
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação).
     * @return Uma Página (Page) de pacientes que correspondem aos filtros.
     */
    @Transactional(readOnly = true)
public Page<Paciente> listarPacientesComFiltros(PacienteFiltroDTO filtro, Pageable pageable) {
    Specification<Paciente> spec = (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();
        
        // Filtro por sintoma (implementação aprimorada)
        if (filtro.getSintoma() != null && !filtro.getSintoma().trim().isEmpty()) {
            // Cria o JOIN com consultas (LEFT JOIN para incluir pacientes sem consultas)
            SetJoin<Paciente, Consulta> consultaJoin = root.joinSet("consultas", JoinType.LEFT);
            
            // Adiciona a condição LIKE para busca parcial case-insensitive
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(consultaJoin.get("sintomasAtuais")),
                "%" + filtro.getSintoma().toLowerCase() + "%"));
            
            // Garante que pacientes não apareçam duplicados se tiverem múltiplas consultas com o mesmo sintoma
            query.distinct(true);
        }
        
        // Outros filtros existentes (nome, sexo, idade etc.)
        // ... (manter sua implementação atual dos outros filtros)
        
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


}