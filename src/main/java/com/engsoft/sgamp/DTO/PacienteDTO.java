package com.engsoft.sgamp.DTO;

import com.engsoft.sgamp.model.Paciente.Sexo;
import lombok.Data;

@Data
public class PacienteDTO {
    private String nomeCompleto;
    private Integer idade;
    private Sexo sexo;
    private String nacionalidade;
    private String localNascimento;
    private String localTrabalhoResidencia;
    private String historicoLocais;
    private String conviventes;
    private String outrosDados;
}
