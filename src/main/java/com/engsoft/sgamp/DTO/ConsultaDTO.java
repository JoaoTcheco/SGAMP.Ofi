package com.engsoft.sgamp.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ConsultaDTO {
    private Long pacienteId;
    private LocalDate dataConsulta;
    private String sintomas;
    private String receitas;
    private String observacoes;
    private String assinaturaMedico;
    private String arquivosExames;
}
