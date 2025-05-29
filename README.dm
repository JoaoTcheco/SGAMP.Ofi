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

