package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String username;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        TECNICO, MEDICO
    }
}