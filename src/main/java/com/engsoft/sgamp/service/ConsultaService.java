package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Consulta;
import com.engsoft.sgamp.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public Optional<Consulta> buscarPorId(Long id) {
        return consultaRepository.findById(id);
    }

    public Consulta salvar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public void deletar(Long id) {
        consultaRepository.deleteById(id);
    }

    public List<Consulta> listarPorPacienteId(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }
    
    // Método adicional opcional
    public List<Consulta> listarPorPacienteIdOrdenadoPorData(Long pacienteId) {
        return consultaRepository.findByPacienteIdOrderByDataConsultaDesc(pacienteId);
    }
}