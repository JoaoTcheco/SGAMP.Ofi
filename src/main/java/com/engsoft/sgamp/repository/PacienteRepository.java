package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Paciente.
 * Permite buscas personalizadas e operações padrão de persistência.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca pacientes cujo nome contenha uma determinada string (ignora caixa).
     *
     * @param nomeParte parte do nome a buscar
     * @return Lista de pacientes encontrados
     */
    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nomeParte);
}