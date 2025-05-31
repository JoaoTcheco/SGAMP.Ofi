package com.engsoft.sm.repository;

import com.engsoft.sm.entity.Consulta;
import com.engsoft.sm.entity.Paciente; // Necessário para o tipo do parâmetro
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Repositório Spring Data JPA para a entidade {@link Consulta}.
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * Busca todas as consultas de um paciente específico, ordenadas pela data da consulta em ordem decrescente (mais recentes primeiro).
     *
     * @param paciente O objeto Paciente para o qual as consultas serão buscadas.
     * @return Uma lista de consultas do paciente, ordenadas.
     */
    List<Consulta> findByPacienteOrderByDataConsultaDesc(Paciente paciente);

    /**
     * Alternativamente, se você preferir buscar pelo ID do paciente:
     * Busca todas as consultas de um paciente específico, pelo ID do paciente,
     * ordenadas pela data da consulta em ordem decrescente.
     *
     * @param pacienteId O ID do Paciente para o qual as consultas serão buscadas.
     * @return Uma lista de consultas do paciente, ordenadas.
     */
    List<Consulta> findByPacienteIdOrderByDataConsultaDesc(Long pacienteId);

    /**
     * Busca consultas dentro de um intervalo de datas.
     *
     * @param inicio Data e hora de início do intervalo.
     * @param fim Data e hora de fim do intervalo.
     * @return Lista de consultas dentro do intervalo especificado.
     */
    List<Consulta> findByDataConsultaBetween(LocalDateTime inicio, LocalDateTime fim);

    // Outros métodos podem ser adicionados, por exemplo, para buscar consultas por médico:
    // List<Consulta> findByMedicoConsultaIdOrderByDataConsultaDesc(Long medicoId);
}