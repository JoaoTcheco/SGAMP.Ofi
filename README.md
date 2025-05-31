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


BANCO DE DADOS:

Criação do Banco de Dados (MySQL)
SQL
CREATE DATABASE IF NOT EXISTS sm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sm_db;

CREATE TABLE medicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,          -- Armazenar hash da senha
    nome_completo VARCHAR(255) NOT NULL,
    -- Adicionar outros campos se necessário, como especialidade, CRM, etc.
    -- Spring Security roles podem ser gerenciadas aqui ou em uma tabela separada (ex: medico_roles)
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    nif VARCHAR(50) UNIQUE,               -- Número de Identificação Fiscal
    documento_identidade VARCHAR(100),    -- BI, Passaporte, etc.
    endereco_completo TEXT,
    contactos_telefonicos VARCHAR(255),   -- Pode ser mais de um, separado por vírgula ou normalizado
    estado_civil VARCHAR(50),             -- Ex: SOLTEIRO, CASADO, etc. (Pode ser um ENUM no Java)
    profissao VARCHAR(150),
    numero_utente_sns VARCHAR(100) UNIQUE, -- Número do Serviço Nacional de Saúde
    nome_familiar_responsavel VARCHAR(255),
    contacto_familiar_responsavel VARCHAR(100),
    sexo VARCHAR(20),                     -- Ex: MASCULINO, FEMININO (Pode ser um ENUM no Java)

    -- Auditoria
    data_cadastro DATETIME NOT NULL,
    data_ultima_atualizacao DATETIME,
    criado_por_medico_id BIGINT,          -- Quem cadastrou o paciente
    atualizado_por_medico_id BIGINT,      -- Quem atualizou por último

    FOREIGN KEY (criado_por_medico_id) REFERENCES medicos(id),
    FOREIGN KEY (atualizado_por_medico_id) REFERENCES medicos(id)
);

CREATE TABLE consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_consulta_id BIGINT NOT NULL,   -- Médico que realizou a consulta
    data_consulta DATETIME NOT NULL,

    -- Dados Clínicos
    historia_medica TEXT,
    sintomas_atuais TEXT,
    resultados_exames TEXT,               -- Pode ser link para arquivos ou descrição
    informacoes_situacao_clinica TEXT,    -- Diagnóstico, prognóstico, plano de tratamento
    medicacao_em_uso TEXT,
    alergias TEXT,
    historico_vacinacao TEXT,
    sinais_vitais TEXT,                   -- Ex: "PA: 120/80 mmHg, FC: 75bpm, Temp: 36.5°C"
    dados_imagem TEXT,                    -- Links para imagens ou referências
    dados_geneticos TEXT,

    -- Auditoria
    data_criacao DATETIME NOT NULL,
    -- data_ultima_atualizacao DATETIME, -- Se consultas puderem ser editadas

    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE, -- Se deletar paciente, deleta consultas
    FOREIGN KEY (medico_consulta_id) REFERENCES medicos(id)
);

-- Adicionar Índices para otimizar buscas
CREATE INDEX idx_paciente_nome ON pacientes(nome_completo);
CREATE INDEX idx_consulta_paciente_data ON consultas(paciente_id, data_consulta DESC);
