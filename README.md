# SGAMP.Ofi
Trabalho contrusao de sistema de gestao de arquivos medicos de pacientes
 
contrucao a cago de Fernando e João docente Ataidé .......que que o senhor nos abençoe
AMEM


fernado cambio
joao recebido
fernando conecao feita termino...

teste concecao com o repositorio gith hub e pc teste teste
conexao recebida cambio bom.
teste 2
teste3
maluco baixar as seguintes extensoes pack for java,,,spring boot, auto close tag , dracula, better comments ,,,material theme, editor config for vs code,,,, gitlens,, html csss support,,intellicode,, js,,live server ,,,mysql
1



para run:    mvn spring-boot:run


CREATE DATABASE IF NOT EXISTS sgamp CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE sgamp;

-- Tabela de usuários (médicos e técnicos)
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role ENUM('TECNICO', 'MEDICO') NOT NULL
);

-- Tabela de pacientes
CREATE TABLE pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    idade INT,
    sexo ENUM('MASCULINO', 'FEMININO', 'OUTRO'),
    nacionalidade VARCHAR(50),
    local_nascimento VARCHAR(100),
    local_trabalho_residencia VARCHAR(150),
    historico_locais TEXT,
    conviventes TEXT,
    outros_dados TEXT
);

-- Tabela de consultas
CREATE TABLE consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    data_consulta DATE NOT NULL,
    sintomas TEXT,
    receitas TEXT,
    observacoes TEXT,
    assinatura_medico VARCHAR(100),
    arquivos_exames TEXT,
    criado_por BIGINT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    FOREIGN KEY (criado_por) REFERENCES usuarios(id)
);

pom



aqui todas classes tou a por comencando por 

model;

usuario

package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

        