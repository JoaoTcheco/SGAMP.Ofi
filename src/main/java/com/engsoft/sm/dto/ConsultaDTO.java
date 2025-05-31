package com.engsoft.sm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent; // Data da consulta pode ser hoje ou no passado
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Consulta, usado em formulários de criação.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {

    private Long id; // Pode ser útil se futuramente houver edição de consultas

    // O ID do paciente e do médico que realiza a consulta não vêm do formulário de dados da consulta em si,
    // mas sim do contexto (ex: URL ou sessão). Portanto, não são campos obrigatórios aqui.
    // private Long pacienteId;
    // private Long medicoId;

    @NotNull(message = "Data da consulta é obrigatória.")
    @PastOrPresent(message = "Data da consulta não pode ser no futuro.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Formato padrão para LocalDateTime
    private LocalDateTime dataConsulta;

    private String historiaMedica;
    private String sintomasAtuais;
    private String resultadosExames;
    private String informacoesSituacaoClinica;
    private String medicacaoEmUso;
    private String alergias;
    private String historicoVacinacao;
    private String sinaisVitais;
    private String dadosImagem;
    private String dadosGeneticos;

    // O ID do paciente ao qual esta consulta pertence.
    // Não é preenchido pelo formulário da consulta em si, mas necessário para o serviço.
    // Pode ser omitido se o serviço sempre receber o pacienteId separadamente.
    // private Long pacienteId;
}