package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Consulta;
import com.engsoft.sgamp.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Consulta.
 * Responsável por persistir e consultar consultas médicas.
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * Retorna todas as consultas de um determinado paciente.
     *
     * @param paciente Entidade paciente
     * @return Lista de consultas encontradas
     */
    List<Consulta> findByPaciente(Paciente paciente);

    /**
     * Retorna consultas de um paciente específico ordenadas por data (mais recentes primeiro).
     *
     * @param paciente Entidade paciente
     * @return Lista de consultas ordenadas
     */
    List<Consulta> findByPacienteOrderByDataConsultaDesc(Paciente paciente);
}