package com.engsoft.sm.entity;

import jakarta.persistence.*; // JPA (Java Persistence API) annotations
import lombok.Getter;       // Lombok annotation to generate getters
import lombok.Setter;       // Lombok annotation to generate setters
import lombok.NoArgsConstructor; // Lombok annotation for no-args constructor
import lombok.AllArgsConstructor; // Lombok annotation for all-args constructor
import java.util.List;      // For lists of related entities


@Entity // Marca esta classe como uma entidade JPA (mapeada para uma tabela no banco)
@Table(name = "medicos") // Especifica o nome da tabela no banco de dados
@Getter // Lombok: Gera automaticamente os métodos getters para todos os campos
@Setter // Lombok: Gera automaticamente os métodos setters para todos os campos
@NoArgsConstructor // Lombok: Gera um construtor sem argumentos (requerido pelo JPA)
@AllArgsConstructor // Lombok: Gera um construtor com todos os argumentos
public class Medico {

    @Id // Marca este campo como a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a geração automática do ID pelo banco (auto-incremento)
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // Define que a coluna não pode ser nula, deve ser única e tem tamanho máximo de 100
    private String username; // Nome de usuário para login

    @Column(nullable = false, length = 255) // Senha (será armazenada como hash)
    private String senha;

    @Column(name = "nome_completo", nullable = false, length = 255) // Nome completo do médico
    private String nomeCompleto;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE") // Define se o médico está ativo no sistema
    private Boolean ativo = true;

    
    @OneToMany(mappedBy = "criadoPorMedico", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Paciente> pacientesCadastrados;

    // Um médico pode ter realizado várias consultas
    @OneToMany(mappedBy = "medicoConsulta", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Consulta> consultasRealizadas;

} 