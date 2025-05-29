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