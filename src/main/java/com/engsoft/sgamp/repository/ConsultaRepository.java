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

    List<Consulta> findByPaciente(Paciente paciente);
    
    List<Consulta> findByPacienteOrderByDataConsultaDesc(Paciente paciente);
    
    // Método para buscar por ID do paciente
    List<Consulta> findByPacienteId(Long pacienteId);
    
    // Opcional: Método para buscar ordenado por data
    List<Consulta> findByPacienteIdOrderByDataConsultaDesc(Long pacienteId);
}