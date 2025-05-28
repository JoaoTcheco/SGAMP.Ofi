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




aquii vamos por anotacoes maluco 

Sistema de gestão de arquivos médicos de pacientes
É um sistema que serve para guardar os dados dos pacientes 
É um sistema que os médicos vão usar para fazer as suas avaliações em pacientes terão nesse sistema o histórico dos seus pacientes
O medico faz login 
depois vai a tela Painel e clica no botão cadastrar paciente, 
apos fazer o cadastro do paciente com os dados pessoas.  ele volta a tela painel e clica no botão procurar paciente pode procurar o paciente cadastrado pelo nome e clica no nome do paciente para abrir a tela paciente
na tela paciente tem uma secção com os dados pessoais e tem uma secção para preencher dados da consulta depois de preencher dados da consulta clica em salvar 
na tela de listar paciente o medico pode fazer listar todos pacientes por condições que quiser podendo ser listar por idade e sexo e tipo da doença entre outras condições.

Telas do sistema:
1 Tela de login
2 Tela Painel com as opções cadastrar paciente, procurar paciente, listar pacientes.
3 Tela de cadastrar paciente
4 Tela procurar paciente
5 tela paciente 
6 Tela listar paciente.


na tela paciente tem uma secção com os dados pessoais e tem outra secção com scroll onde tem as consultas onde a cada consulta o medico vai por todos dados da consulta. Data, a assinatura do medico, receitas sintomas, e outros dados da consulta.

Se o paciente voltar de novo para consulta O medico faz login vai na tela de procurar paciente e digita o nome do paciente, e clica no nome do paciente ao abrir a tela paciente ele vai editar abrir nova consulta onde vai adicionar as informações da nova consulta e assim vai se criando o histórico do paciente
O sistema deve ser simples como descrito acima.

1.	Login
- Tela de autenticação segura para médicos e técnicos
- Campos obrigatórios:
  - Nome de usuário
  - Senha
- Botão "Entrar" para acessar o sistema

### 2. Tela Painel
Após login bem-sucedido, o usuário acessa o menu com três opções:
1. **Criar Paciente** - Para cadastro de novos pacientes
2. **Procurar Paciente** - Para buscar e acessar registros existentes
3. **Listar** - Para gerar lista de doentes por condicoes 



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


# Configuração do banco de dados MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/sgamp?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=  # coloque sua senha do MySQL aqui

# Dialeto e JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Thymeleaf
spring.thymeleaf.cache=false

# Porta do servidor
server.port=8080

# Segurança - redirecionamento após login
spring.security.user.name=admin
spring.security.user.password=admin
spring.security.user.roles=TECNICO

