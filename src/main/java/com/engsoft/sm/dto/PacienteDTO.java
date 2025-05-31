package com.engsoft.sm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past; // Para garantir que a data de nascimento seja no passado
import jakarta.validation.constraints.Size;
// import jakarta.validation.constraints.Email; // Se tivesse campo de email
import org.springframework.format.annotation.DateTimeFormat; // Para formatação de data
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para transferência de dados de Paciente, usado em formulários de criação e atualização.
 * Inclui anotações de validação para os campos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    // O ID não é geralmente parte do DTO de criação, mas pode ser útil para atualização.
    // Se este DTO for usado tanto para criar quanto para atualizar, o ID pode ser nulo na criação.
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório.")
    @Size(min = 3, max = 255, message = "Nome completo deve ter entre 3 e 255 caracteres.")
    private String nomeCompleto;

    @NotNull(message = "Data de nascimento é obrigatória.")
    @Past(message = "Data de nascimento deve ser uma data no passado.")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Ajuda o Spring a converter String para LocalDate
    private LocalDate dataNascimento;

    @Size(max = 50, message = "NIF deve ter no máximo 50 caracteres.")
    private String nif; // Não obrigatório, mas com limite de tamanho

    @Size(max = 100, message = "Documento de identidade deve ter no máximo 100 caracteres.")
    private String documentoIdentidade;

    @Size(max = 1000, message = "Endereço completo deve ter no máximo 1000 caracteres.") // Ajuste o tamanho conforme necessário
    private String enderecoCompleto;

    @Size(max = 255, message = "Contatos telefônicos devem ter no máximo 255 caracteres.")
    private String contactosTelefonicos;

    @Size(max = 50, message = "Estado civil deve ter no máximo 50 caracteres.")
    private String estadoCivil;

    @Size(max = 150, message = "Profissão deve ter no máximo 150 caracteres.")
    private String profissao;

    @Size(max = 100, message = "Número de utente do SNS deve ter no máximo 100 caracteres.")
    private String numeroUtenteSNS;

    @Size(max = 255, message = "Nome do familiar/responsável deve ter no máximo 255 caracteres.")
    private String nomeFamiliarResponsavel;

    @Size(max = 100, message = "Contato do familiar/responsável deve ter no máximo 100 caracteres.")
    private String contactoFamiliarResponsavel;

    @Size(max = 20, message = "Sexo deve ter no máximo 20 caracteres.")
    private String sexo; // Ex: "Masculino", "Feminino"

    // Campos de auditoria como dataCadastro, criadoPorMedico não são preenchidos pelo usuário,
    // então não precisam estar neste DTO (serão tratados no serviço).
}