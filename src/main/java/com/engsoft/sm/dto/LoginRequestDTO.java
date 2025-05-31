package com.engsoft.sm.dto;

import jakarta.validation.constraints.NotBlank; // Para validação de campos não vazios
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO (Data Transfer Object) para encapsular os dados da requisição de login.
 * Contém o nome de usuário e a senha fornecidos pelo médico.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "O nome de usuário não pode estar em branco.") // Validação: campo obrigatório
    private String username;

    @NotBlank(message = "A senha não pode estar em branco.") // Validação: campo obrigatório
    private String senha; // Nome do campo como 'senha' para corresponder ao formulário de login padrão do Spring Security
}