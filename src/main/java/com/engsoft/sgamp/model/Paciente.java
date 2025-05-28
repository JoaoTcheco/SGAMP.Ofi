package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String nacionalidade;

    @Column(name = "local_nascimento")
    private String localNascimento;

    @Column(name = "local_trabalho_residencia")
    private String localTrabalhoResidencia;

    @Column(name = "historico_locais", columnDefinition = "TEXT")
    private String historicoLocais;

    @Column(columnDefinition = "TEXT")
    private String conviventes;

    @Column(name = "outros_dados", columnDefinition = "TEXT")
    private String outrosDados;

    public enum Sexo {
        MASCULINO, FEMININO, OUTRO
    }
}
