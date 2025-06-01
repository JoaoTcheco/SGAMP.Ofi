package com.engsoft.sm.dto;

import com.engsoft.sm.entity.Consulta; // Precisamos da entidade Consulta ou um ConsultaResumoDTO
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para exibir os detalhes de um Paciente, incluindo suas consultas.
 * Este DTO é uma combinação de dados de Paciente e uma lista de suas Consultas (ou ConsultaDTOs).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDetalhesDTO {

    // Dados do Paciente (pode ser uma cópia dos campos de PacienteDTO ou os mais relevantes para exibição)
    private Long id;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String nuit;
    private String documentoIdentidade;
    private String enderecoCompleto;
    private String contactosTelefonicos;
    private String estadoCivil;
    private String profissao;
    private String numeroPacienteNP;
    private String nomeFamiliarResponsavel;
    private String contactoFamiliarResponsavel;
    private String sexo;
    
    
    private List<Consulta> consultas;

    
}