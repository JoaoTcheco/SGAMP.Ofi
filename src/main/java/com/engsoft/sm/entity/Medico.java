package com.engsoft.sm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity // Avisa o JPA que isso aqui vai virar tabela
@Table(name = "medicos") // Nome da tabela lá no banco
@Getter // Lombok pra fazer os get
@Setter // Lombok pra fazer os set
@NoArgsConstructor // Lombok pra fazer construtor vazio (JPA precisa)
@AllArgsConstructor // Lombok pra fazer construtor com tudo
public class Medico {

    @Id // Chave primária, né?
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable = false, unique = true, length = 100) 
    private String username; 

    @Column(nullable = false, length = 255) 
    private String senha;

    @Column(name = "nome_completo", nullable = false, length = 255) 
    private String nomeCompleto;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE") 
    private Boolean ativo = true;


    @OneToMany(mappedBy = "criadoPorMedico", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Paciente> pacientesCadastrados;

    // E aqui as consultas que o médico fez.
    @OneToMany(mappedBy = "medicoConsulta", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Consulta> consultasRealizadas;

}