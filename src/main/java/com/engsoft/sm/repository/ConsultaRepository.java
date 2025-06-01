package com.engsoft.sm.repository;

import com.engsoft.sm.entity.Consulta;
import com.engsoft.sm.entity.Paciente; // Necessário para o tipo do parâmetro
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Repositório Spring Data JPA para a entidade 
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    
    List<Consulta> findByPacienteOrderByDataConsultaDesc(Paciente paciente);

    List<Consulta> findByPacienteIdOrderByDataConsultaDesc(Long pacienteId);

    List<Consulta> findByDataConsultaBetween(LocalDateTime inicio, LocalDateTime fim);

    // Outros métodos podem ser adicionados, por exemplo, para buscar consultas por médico:
    // List<Consulta> findByMedicoConsultaIdOrderByDataConsultaDesc(Long medicoId);
}