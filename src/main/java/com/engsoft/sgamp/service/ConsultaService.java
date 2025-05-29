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