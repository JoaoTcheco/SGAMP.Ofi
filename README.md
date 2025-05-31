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

    public enum Role {
        TECNICO,
        MEDICO
    }
}

paciente:

package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;
    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String nacionalidade;
    private String localNascimento;
    private String localTrabalhoResidencia;

    @Lob
    private String historicoLocais;

    @Lob
    private String conviventes;

    @Lob
    private String outrosDados;

    public enum Sexo {
        MASCULINO,
        FEMININO,
        OUTRO
    }
}


consulta:
package com.engsoft.sgamp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate dataConsulta;

    @Lob
    private String sintomas;

    @Lob
    private String receitas;

    @Lob
    private String observacoes;

    private String assinaturaMedico;

    @Lob
    private String arquivosExames;

    @ManyToOne
    private Usuario criadoPor;
}


pacote Repository

ConsultaRepository

package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Consulta;
import com.engsoft.sgamp.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPaciente(Paciente paciente);
    List<Consulta> findByPacienteAndDataConsulta(Paciente paciente, LocalDate dataConsulta);
    List<Consulta> findByDataConsultaBetween(LocalDate inicio, LocalDate fim);
}



PacienteRepository

package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);
    
    @Query("SELECT p FROM Paciente p WHERE " +
           "(:nome IS NULL OR p.nomeCompleto LIKE %:nome%) AND " +
           "(:idade IS NULL OR p.idade = :idade) AND " +
           "(:sexo IS NULL OR p.sexo = :sexo)")
    List<Paciente> findByFiltros(
            @Param("nome") String nome,
            @Param("idade") Integer idade,
            @Param("sexo") Paciente.Sexo sexo);
}



UsuarioRepository~

package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}


pacote config

WebSecurityConfig

package com.engsoft.sgamp.config;

import com.engsoft.sgamp.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @SuppressWarnings("unused")
    private final AuthService authService;

    public WebSecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/login", "/static/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/painel")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


pacote servico

ConsultaServico

package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Consulta;
import com.engsoft.sgamp.model.Paciente;
import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public Consulta salvarConsulta(Consulta consulta, Usuario medico) {
        consulta.setCriadoPor(medico);
        if (consulta.getDataConsulta() == null) {
            consulta.setDataConsulta(LocalDate.now());
        }
        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarConsultasPorPaciente(Paciente paciente) {
        return consultaRepository.findByPaciente(paciente);
    }

    public List<Consulta> listarConsultasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return consultaRepository.findByDataConsultaBetween(inicio, fim);
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id).orElse(null);
    }
}


PacienteService

package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Paciente;
import com.engsoft.sgamp.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente salvarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeCompletoContainingIgnoreCase(nome);
    }

    public List<Paciente> listarPorFiltros(String nome, Integer idade, Paciente.Sexo sexo) {
        return pacienteRepository.findByFiltros(nome, idade, sexo);
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }
}



UsuarioService

package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public boolean usernameDisponivel(String username) {
        return !usuarioRepository.existsByUsername(username);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }
}


AuthService

package com.engsoft.sgamp.service;

import com.engsoft.sgamp.DTO.LoginDTO;
import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, 
                      UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),
                    loginDTO.getSenha()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public Usuario registerMedico(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
}


pom.xml

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.0</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.engsoft</groupId>
	<artifactId>sgamp</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>sgamp</name>
	<description>Sistema de gestão de arquivos médicos de pacientes</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.thymeleaf.extras</groupId>
			<artifactId>thymeleaf-extras-springsecurity6</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
   			 <groupId>org.projectlombok</groupId>
   			 <artifactId>lombok</artifactId>
   			 <optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>



para primeira atualizacao------------------------------------------------------------------
# Implementação do Sistema de Login

Vou criar as classes necessárias e a tela de login para tornar o processo de autenticação funcional. Aqui estão as atualizações necessárias:

## 1. Primeiro, vamos criar o DTO para login:

`src/main/java/com/engsoft/sgamp/DTO/LoginDTO.java`
```java
package com.engsoft.sgamp.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String senha;
}
```

## 2. Configuração de Segurança:

`src/main/java/com/engsoft/sgamp/config/SecurityConfig.java`
```java
package com.engsoft.sgamp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/painel", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Adicione este método para disponibilizar o AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}





```

## 3. Controlador para autenticação:

`src/main/java/com/engsoft/sgamp/controller/AuthController.java`
```java
package com.engsoft.sgamp.controller;

import com.engsoft.sgamp.DTO.LoginDTO;
import com.engsoft.sgamp.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping
    public String login(LoginDTO loginDTO, Model model) {
        if (authService.authenticate(loginDTO)) {
            return "redirect:/painel";
        }
        model.addAttribute("error", "Credenciais inválidas");
        return "login";
    }
}
```..............................

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SGAMP - Login</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        
        .login-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            padding: 40px;
            width: 350px;
            text-align: center;
        }
        
        .logo {
            margin-bottom: 30px;
        }
        
        .logo h1 {
            color: #2c3e50;
            margin: 0;
            font-size: 24px;
        }
        
        .logo p {
            color: #7f8c8d;
            margin: 5px 0 0;
            font-size: 14px;
        }
        
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #2c3e50;
            font-weight: 500;
        }
        
        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #3498db;
        }
        
        .error-message {
            color: #e74c3c;
            font-size: 13px;
            margin-top: 5px;
            display: none;
        }
        
        .login-button {
            width: 100%;
            padding: 12px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .login-button:hover {
            background-color: #2980b9;
        }
        
        .footer {
            margin-top: 20px;
            font-size: 13px;
            color: #7f8c8d;
        }
        
        .alert {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
            font-size: 14px;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="logo">
            <h1>SGAMP</h1>
            <p>Sistema de Gestão de Arquivos Médicos de Pacientes</p>
        </div>
        
        <div th:if="${error}" class="alert alert-danger" th:text="${error}"></div>
        
        <form th:action="@{/login}" th:object="${loginDTO}" method="post">
            <div class="form-group">
                <label for="username">Nome de Usuário</label>
                <input type="text" id="username" th:field="*{username}" required>
                <div class="error-message" id="username-error"></div>
            </div>
            
            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" th:field="*{senha}" required>
                <div class="error-message" id="password-error"></div>
            </div>
            
            <button type="submit" class="login-button">Entrar</button>
        </form>
        
        <div class="footer">
            Sistema exclusivo para profissionais de saúde
        </div>
    </div>

    <script>
        document.querySelector('form').addEventListener('submit', function(e) {
            let isValid = true;
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('senha').value.trim();
            
            // Validação simples do lado do cliente
            if (!username) {
                document.getElementById('username-error').textContent = 'Por favor, insira o nome de usuário';
                document.getElementById('username-error').style.display = 'block';
                isValid = false;
            } else {
                document.getElementById('username-error').style.display = 'none';
            }
            
            if (!password) {
                document.getElementById('password-error').textContent = 'Por favor, insira a senha';
                document.getElementById('password-error').style.display = 'block';
                isValid = false;
            } else {
                document.getElementById('password-error').style.display = 'none';
            }
            
            if (!isValid) {
                e.preventDefault();
            }
        });
    </script>
</body>
</html>



-------------------------------------------------
-------------------------------------------------------
---------------------------------------------
no form metodo forme mudar por no login  <form th:action="@{/login}" method="post">




<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SGAMP - Painel</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 0;
        }
        
        .header {
            background-color: #3498db;
            color: white;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .logo {
            display: flex;
            align-items: center;
        }
        
        .logo h1 {
            margin: 0;
            font-size: 24px;
        }
        
        .user-info {
            display: flex;
            align-items: center;
        }
        
        .user-info span {
            margin-right: 15px;
        }
        
        .logout-btn {
            background-color: #e74c3c;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
        }
        
        .logout-btn:hover {
            background-color: #c0392b;
        }
        
        .main-content {
            padding: 30px;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .welcome-message {
            background-color: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        
        .options-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
        }
        
        .option-card {
            background-color: white;
            border-radius: 8px;
            padding: 25px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
        }
        
        .option-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0,0,0,0.1);
        }
        
        .option-card h2 {
            color: #2c3e50;
            margin-top: 0;
            margin-bottom: 10px;
        }
        
        .option-card p {
            color: #7f8c8d;
            margin-bottom: 0;
        }
        
        .option-icon {
            font-size: 40px;
            margin-bottom: 15px;
            color: #3498db;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo">
            <h1>SGAMP</h1>
        </div>
        <div class="user-info">
            <span th:text="'Bem-vindo, ' + ${#authentication.name}"></span>
            <form th:action="@{/logout}" method="post">
                <button type="submit" class="logout-btn">Sair</button>
            </form>
        </div>
    </div>
    
    <div class="main-content">
        <div class="welcome-message">
            <h2>Painel Principal</h2>
            <p>Selecione uma das opções abaixo para gerenciar seus pacientes e consultas.</p>
        </div>
        
        <div class="options-grid">
            <div class="option-card" onclick="window.location.href='/cadastrar-paciente'">
                <div class="option-icon">➕</div>
                <h2>Criar Paciente</h2>
                <p>Cadastre um novo paciente no sistema com seus dados pessoais e histórico médico.</p>
            </div>
            
            <div class="option-card" onclick="window.location.href='/procurar-paciente'">
                <div class="option-icon">🔍</div>
                <h2>Procurar Paciente</h2>
                <p>Busque pacientes existentes por nome para acessar seus registros e histórico.</p>
            </div>
            
            <div class="option-card" onclick="window.location.href='/listar-pacientes'">
                <div class="option-icon">📋</div>
                <h2>Listar Pacientes</h2>
                <p>Visualize listas de pacientes filtrados por condições específicas como idade, sexo ou diagnóstico.</p>
            </div>
        </div>
    </div>
</body>
</html>


------------------
3. Atualização do SecurityConfig
src/main/java/com/engsoft/sgamp/config/WebSecurityConfig.java

package com.engsoft.sgamp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                    "/",
                    "/login",
                    "/static/**",
                    "/css/**",
                    "/js/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/painel", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


4. Controlador do Painel
src/main/java/com/engsoft/sgamp/controller/PainelController.java

package com.engsoft.sgamp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelController {

    @GetMapping("/painel")
    public String painel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "painel";
    }
}


5. Atualização do application.properties
src/main/resources/application.properties

# Configurações do Spring Security
spring.security.user.name=admin
spring.security.user.password=admin
spring.security.user.roles=MEDICO

# Configuração para redirecionamento após logout
spring.security.logout.redirect-url=/login?logout





2. Configuração de logout:
properties
spring.security.logout.redirect-url=/login?logout
Esta linha define:

Para onde o usuário será redirecionado após fazer logout

O parâmetro ?logout pode ser usado para mostrar uma mensagem na tela de login confirmando que o logout foi bem-sucedido
