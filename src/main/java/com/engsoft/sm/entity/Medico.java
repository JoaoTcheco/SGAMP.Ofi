package com.engsoft.sm.entity;

import jakarta.persistence.*; // JPA (Java Persistence API) annotations
import lombok.Getter;       // Lombok annotation to generate getters
import lombok.Setter;       // Lombok annotation to generate setters
import lombok.NoArgsConstructor; // Lombok annotation for no-args constructor
import lombok.AllArgsConstructor; // Lombok annotation for all-args constructor
import java.util.List;      // For lists of related entities

/**
 * Entidade JPA que representa um Médico no sistema.
 * Um médico é um usuário que pode fazer login e gerenciar pacientes e consultas.
 */
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

    // Relacionamentos (serão mapeados mais tarde se necessário, mas vamos declarar para integridade conceitual)
    // Um médico pode ter cadastrado vários pacientes
    // mappedBy = "criadoPorMedico": Indica que o lado 'Medico' é o inverso do relacionamento
    // e o mapeamento é definido no campo 'criadoPorMedico' da entidade 'Paciente'.
    // CascadeType.ALL: Operações de persistência (salvar, atualizar, deletar) no Medico serão cascateadas para os Pacientes relacionados.
    //                  Use com cautela, especialmente CascadeType.REMOVE ou ALL.
    // FetchType.LAZY: Pacientes não são carregados do banco a menos que explicitamente acessados.
    @OneToMany(mappedBy = "criadoPorMedico", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Paciente> pacientesCadastrados;

    // Um médico pode ter realizado várias consultas
    @OneToMany(mappedBy = "medicoConsulta", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Consulta> consultasRealizadas;


    // Construtor customizado (opcional, se AllArgsConstructor não for suficiente)
    // Exemplo:
    // public Medico(String username, String senha, String nomeCompleto) {
    //     this.username = username;
    //     this.senha = senha;
    //     this.nomeCompleto = nomeCompleto;
    //     this.ativo = true;
    // }

    // Lombok já gera getters e setters, toString, equals, hashCode se você usar @Data
    // ou @Getter, @Setter, @ToString, @EqualsAndHashCode individualmente.
    // Por simplicidade, @Getter e @Setter são suficientes para começar.~


    
} 