package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @Column(columnDefinition = "TEXT")
    private String sintomas;

    @Column(columnDefinition = "TEXT")
    private String receitas;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "assinatura_medico")
    private String assinaturaMedico;

    @Column(name = "arquivos_exames", columnDefinition = "TEXT")
    private String arquivosExames;

    @ManyToOne
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;
}
