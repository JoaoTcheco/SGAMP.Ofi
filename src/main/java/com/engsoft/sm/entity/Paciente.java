package com.engsoft.sm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate; // Para data de nascimento
import java.time.LocalDateTime; // Para data de cadastro/atualização
import java.util.List;

/**
 * Entidade JPA que representa um Paciente.
 * Contém dados pessoais e está associado a um histórico de consultas.
 */
@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento; // Apenas data, sem hora

    @Column(length = 50, unique = true) // NUIT pode ser único
    private String nuit;

    @Column(name = "documento_identidade", length = 100)
    private String documentoIdentidade;

    @Column(name = "endereco_completo", columnDefinition = "TEXT") // Para textos mais longos
    private String enderecoCompleto;

    @Column(name = "contactos_telefonicos", length = 255)
    private String contactosTelefonicos; // Pode armazenar múltiplos, separados por vírgula

    @Column(name = "estado_civil", length = 50)
    private String estadoCivil; // Ex: "Solteiro(a)", "Casado(a)". Poderia ser um Enum.

    @Column(length = 150)
    private String profissao;

    @Column(name = "numero_paciente_np", length = 100, unique = true) // Pode ser único
    private String numeroPacienteNP;

    @Column(name = "nome_familiar_responsavel", length = 255)
    private String nomeFamiliarResponsavel;

    @Column(name = "contacto_familiar_responsavel", length = 100)
    private String contactoFamiliarResponsavel;

    @Column(length = 20)
    private String sexo; // Ex: "Masculino", "Feminino". Poderia ser um Enum.

    // --- Auditoria ---
    @Column(name = "data_cadastro", nullable = false, updatable = false) // Não pode ser atualizado após criação
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataUltimaAtualizacao;

    // Relacionamento: Médico que cadastrou o paciente
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: Carrega o médico apenas quando acessado
    @JoinColumn(name = "criado_por_medico_id", referencedColumnName = "id") // Chave estrangeira na tabela 'pacientes'
    private Medico criadoPorMedico;

    // Relacionamento: Médico que atualizou por último (pode ser nulo inicialmente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atualizado_por_medico_id", referencedColumnName = "id")
    private Medico atualizadoPorMedico;

   
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Consulta> consultas;

    // Método utilitário para adicionar uma consulta ao paciente (mantém os dois lados do relacionamento sincronizados)
    public void adicionarConsulta(Consulta consulta) {
        this.consultas.add(consulta);
        consulta.setPaciente(this);
    }

    public void removerConsulta(Consulta consulta) {
        this.consultas.remove(consulta);
        consulta.setPaciente(null);
    }

    // Antes de persistir um novo paciente, definir a data de cadastro
    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    // Antes de atualizar um paciente, definir a data da última atualização
    @PreUpdate
    protected void onUpdate() {
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }
}
