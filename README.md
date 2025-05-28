# SGAMP.Ofi
Trabalho contrusao de sistema de gestao de arquivos medicos de pacientes
 
contrucao a cago de Fernando e João docente Ataidé .......que que o senhor nos abençoe
AMEM


fernado cambio


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



### Tela Cadastro Paciente 
Dividido em duas seções principais:
*secao 1 Dados Pessoais:**
- Nome completo
- Idade
- Sexo
- Nacionalidade
- Local de nascimento
- Local de trabalho/residência
- Histórico de locais onde viveu
- Pessoas com quem vive/viveu
- Outros dados contextuais relevantes para avaliação médica

2.	Seccao 2 

Tela Paciente
secção Consulta 
Edição/Adição de Informações:**
 Para consultas existentes:
  - Edição dos dados clínicos
  - Adição de novas informações
  - Upload de novos exames/imagens
Para novas consultas:
  - Criação de novo registro de consulta com data atual
  - Registro completo dos novos sintomas
  - Novas receitas e recomendações
  - Upload de novos exames
  - Observações médicas

**tela procurar Paciente:**
- Campo de pesquisa por nome (com sugestões durante a digitação)
- Lista de pacientes correspondentes à pesquisa
- Seleção do paciente desejado

**3. Edição/Adição de Informações:**
- Para consultas existentes:
  - Edição dos dados clínicos
  - Adição de novas informações
  - Upload de novos exames/imagens

 Para novas consultas:
  - Criação de novo registro de consulta com data atual
  - Registro completo dos novos sintomas
  - Novas receitas e recomendações
  - Upload de novos exames
  - Observações médicas




**Funcionalidades:**
- Listagem de pacientes por critérios selecionados e personalizados

## Requisitos Técnicos Adicionais
   - Autenticação obrigatória
   - Banco de dados para informações estruturadas mysql
   - Interface intuitiva e limpa
   - Busca eficiente de pacientes
   - Organização clara do histórico médico
   - Sistema de backup regular dos dados



 



O meu xamp as seguintes config
Servidor Web (Apache) Portas: 80 (HTTP) e 443 (HTTPS).
Banco de Dados (MySQL) Porta: 3306 



Descrição do Projeto Spring Initializr Gerado
Configuração Básica do Projeto
O projeto foi configurado com as seguintes características principais:
Metadados do Projeto
•	Nome do Projeto: sgamp
•	Grupo: com.engsoft 
•	Artefato: sgamp
•	Pacote principal: com.engsoft.sgamp
•	Descrição: Sistema de gestão de arquivos médicos de pacientes
Configurações Técnicas
•	Tipo de Projeto: Maven
•	Linguagem Principal: Java
•	Versão do Spring Boot: 3.5.0 
•	Empacotamento: Jar 
•	Versão do Java: 17 
Dependências Selecionadas
1.	Spring Web: Para construção de aplicações web RESTful
2.	Spring Security: Para autenticação e controle de acesso
3.	Spring Data JPA: Para persistência de dados com Hibernate
4.	Lombok: Para redução de código boilerplate
5.	Thymeleaf: Para templates server-side
6.	Validação: Para validação de beans
7.	DevTools: Para melhor experiência de desenvolvimento



sgamp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── engsoft/
│   │   │           └── sgamp/
│   │   │               └── SgampApplication.java  
│   │   └── resources/
│   │       └── application.properties  
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── engsoft/
│       │           └── sgamp/
│       │               └── SgampApplicationTests.java  
│       └── resources/
│
├── target/
│   ├── generated-sources/
│   │   └── annotations/  
│   └── generated-test-sources/
│       └── test-annotations/
│
├── JRE System Library 
├── Maven Dependencies/  
│
├── mvnw  
├── mvnw.cmd  
├── pom.xml  
└── README.md 
