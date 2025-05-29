package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Consulta;
import com.engsoft.sgamp.model.Paciente;
import com.engsoft.sgamp.repository.ConsultaRepository;
import com.engsoft.sgamp.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioService {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    public RelatorioService(PacienteRepository pacienteRepository, ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.consultaRepository = consultaRepository;
    }

    public List<Paciente> gerarRelatorioPacientes(String nome, Integer idade, Paciente.Sexo sexo) {
        return pacienteRepository.findByFiltros(nome, idade, sexo);
    }

    public List<Consulta> gerarRelatorioConsultas(LocalDate dataInicio, LocalDate dataFim) {
        return consultaRepository.findByDataConsultaBetween(dataInicio, dataFim);
    }
}
