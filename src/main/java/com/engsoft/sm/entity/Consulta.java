package com.engsoft.sm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma Consulta Médica.
 * Cada consulta está ligada a um Paciente e a um Médico que a realizou.
 */
@Entity
@Table(name = "consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: Paciente ao qual esta consulta pertence
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Uma consulta DEVE pertencer a um paciente
    @JoinColumn(name = "paciente_id", nullable = false, referencedColumnName = "id")
    private Paciente paciente;

    // Relacionamento: Médico que realizou esta consulta
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Uma consulta DEVE ser realizada por um médico
    @JoinColumn(name = "medico_consulta_id", nullable = false, referencedColumnName = "id")
    private Medico medicoConsulta; // Médico que efetivamente fez esta consulta específica

    @Column(name = "data_consulta", nullable = false)
    private LocalDateTime dataConsulta;

    // --- Dados Clínicos (usando columnDefinition="TEXT" para campos longos) ---
    @Column(name = "historia_medica", columnDefinition = "TEXT")
    private String historiaMedica;

    @Column(name = "sintomas_atuais", columnDefinition = "TEXT")
    private String sintomasAtuais;

    @Column(name = "resultados_exames", columnDefinition = "TEXT")
    private String resultadosExames;

    @Column(name = "informacoes_situacao_clinica", columnDefinition = "TEXT")
    private String informacoesSituacaoClinica; // Diagnóstico, prognóstico, plano

    @Column(name = "medicacao_em_uso", columnDefinition = "TEXT")
    private String medicacaoEmUso;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "historico_vacinacao", columnDefinition = "TEXT")
    private String historicoVacinacao;

    @Column(name = "sinais_vitais", columnDefinition = "TEXT")
    private String sinaisVitais; // Ex: "PA: 120/80 mmHg, FC: 75bpm, Temp: 36.5°C"

    @Column(name = "dados_imagem", columnDefinition = "TEXT")
    private String dadosImagem; // Links ou referências

    @Column(name = "dados_geneticos", columnDefinition = "TEXT")
    private String dadosGeneticos;

    // --- Auditoria ---
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.dataConsulta == null) { // Garante que a data da consulta seja definida se não explicitada
            this.dataConsulta = LocalDateTime.now();
        }
    }
}