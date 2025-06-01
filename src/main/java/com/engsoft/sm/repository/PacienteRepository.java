package com.engsoft.sm.repository;

import com.engsoft.sm.entity.Paciente;
import org.springframework.data.domain.Pageable; // Import para Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Para consultas dinâmicas com Criteria API
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade {@link Paciente}.
 * Além dos métodos CRUD básicos, também estende JpaSpecificationExecutor
 * para permitir a construção de consultas dinâmicas (filtros complexos).
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {

    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);

    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome, Pageable pageable);

    Optional<Paciente> findBynuit(String nuit);

    Optional<Paciente> findBynumeroPacienteNP(String numeroPacienteNP);

    // aqui podemos por outros metodos de busca
}