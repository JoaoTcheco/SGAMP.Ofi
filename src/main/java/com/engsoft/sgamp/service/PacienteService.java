package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Paciente;
import com.engsoft.sgamp.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente salvarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeCompletoContainingIgnoreCase(nome);
    }

    public List<Paciente> listarPorFiltros(String nome, Integer idade, Paciente.Sexo sexo) {
        return pacienteRepository.findByFiltros(nome, idade, sexo);
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }
}