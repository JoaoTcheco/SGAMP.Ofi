package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;
    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String nacionalidade;
    private String localNascimento;
    private String localTrabalhoResidencia;

    @Lob
    private String historicoLocais;

    @Lob
    private String conviventes;

    @Lob
    private String outrosDados;

    public enum Sexo {
        MASCULINO,
        FEMININO,
        OUTRO
    }
}


