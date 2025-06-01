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


sistema de Joao tcheco e fernando cuna boom 


banco de dados 

-- Cria a base de dados se ela não existir, com a codificação correta
CREATE DATABASE IF NOT EXISTS sm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Seleciona a base de dados para uso
USE sm_db;

-- Cria a tabela 'medicos'
CREATE TABLE medicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL, -- Armazenar hash da senha
    nome_completo VARCHAR(255) NOT NULL,
    -- Adicionar outros campos se necessário, como especialidade, CRM, etc.
    ativo BOOLEAN DEFAULT TRUE
);

-- Cria a tabela 'pacientes' com as correções para NUIT e numero_paciente_np
CREATE TABLE pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    -- NUIT: Pode ser nulo, é único se preenchido, e o padrão é NULL
    nuit VARCHAR(50) UNIQUE NULL DEFAULT NULL,
    documento_identidade VARCHAR(100) NULL DEFAULT NULL,
    endereco_completo TEXT NULL DEFAULT NULL,
    contactos_telefonicos VARCHAR(255) NULL DEFAULT NULL,
    estado_civil VARCHAR(50) NULL DEFAULT NULL,
    profissao VARCHAR(150) NULL DEFAULT NULL,
    -- numero_paciente_np: Pode ser nulo, é único se preenchido, e o padrão é NULL
    numero_paciente_np VARCHAR(100) UNIQUE NULL DEFAULT NULL,
    nome_familiar_responsavel VARCHAR(255) NULL DEFAULT NULL,
    contacto_familiar_responsavel VARCHAR(100) NULL DEFAULT NULL,
    sexo VARCHAR(20) NULL DEFAULT NULL,

    -- Auditoria
    data_cadastro DATETIME NOT NULL,
    data_ultima_atualizacao DATETIME NULL DEFAULT NULL,
    criado_por_medico_id BIGINT NULL DEFAULT NULL,
    atualizado_por_medico_id BIGINT NULL DEFAULT NULL,

    FOREIGN KEY (criado_por_medico_id) REFERENCES medicos(id),
    FOREIGN KEY (atualizado_por_medico_id) REFERENCES medicos(id)
);

-- Cria a tabela 'consultas'
CREATE TABLE consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_consulta_id BIGINT NOT NULL, -- Médico que realizou a consulta
    data_consulta DATETIME NOT NULL,

    -- Dados Clínicos
    historia_medica TEXT NULL DEFAULT NULL,
    sintomas_atuais TEXT NULL DEFAULT NULL,
    resultados_exames TEXT NULL DEFAULT NULL,
    informacoes_situacao_clinica TEXT NULL DEFAULT NULL,
    medicacao_em_uso TEXT NULL DEFAULT NULL,
    alergias TEXT NULL DEFAULT NULL,
    historico_vacinacao TEXT NULL DEFAULT NULL,
    sinais_vitais TEXT NULL DEFAULT NULL,
    dados_imagem TEXT NULL DEFAULT NULL,
    dados_geneticos TEXT NULL DEFAULT NULL,

    -- Auditoria
    data_criacao DATETIME NOT NULL,
    -- data_ultima_atualizacao DATETIME NULL DEFAULT NULL, -- Descomente se consultas puderem ser editadas

    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_consulta_id) REFERENCES medicos(id)
);

-- Adicionar Índices para otimizar buscas
CREATE INDEX idx_paciente_nome ON pacientes(nome_completo);
CREATE INDEX idx_paciente_nuit ON pacientes(nuit);
CREATE INDEX idx_paciente_numero_paciente_np ON pacientes(numero_paciente_np);
CREATE INDEX idx_consulta_paciente_data ON consultas(paciente_id, data_consulta DESC);

serice paciente melhoradao:
