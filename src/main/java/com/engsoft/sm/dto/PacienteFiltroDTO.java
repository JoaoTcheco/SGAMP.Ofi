package com.engsoft.sm.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para encapsular os critérios de filtro ao listar pacientes.
 * Os campos representam os possíveis filtros que o usuário pode aplicar.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteFiltroDTO {

    private String nome; // Para busca por nome
    private Integer idadeMinima;
    private Integer idadeMaxima;
    private String sexo;
    private String sintoma; // Campo genérico para busca em sintomas
    private String doenca;  // Campo genérico para busca em histórico ou /diagnostico

   
}