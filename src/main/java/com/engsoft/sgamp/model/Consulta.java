package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate dataConsulta;

    @Lob
    private String sintomas;

    @Lob
    private String receitas;

    @Lob
    private String observacoes;

    private String assinaturaMedico;

    @Lob
    private String arquivosExames;

    @ManyToOne
    private Usuario criadoPor;
}
