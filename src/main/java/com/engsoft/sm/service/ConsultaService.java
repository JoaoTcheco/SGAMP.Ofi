package com.engsoft.sm.service;

import com.engsoft.sm.dto.ConsultaDTO;
import com.engsoft.sm.entity.Consulta;
import com.engsoft.sm.entity.Medico;
import com.engsoft.sm.entity.Paciente;
import com.engsoft.sm.repository.ConsultaRepository;
import com.engsoft.sm.repository.MedicoRepository;
import com.engsoft.sm.repository.PacienteRepository;
import com.engsoft.sm.exception.ResourceNotFoundException;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

/**
 * Serviço para gerenciar a lógica de negócios relacionada a Consultas Médicas.
 */
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;


    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    
    @Transactional
    public Consulta criarConsulta(Long pacienteId, ConsultaDTO consultaDTO, String medicoLogadoUsername) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", pacienteId));

        Medico medicoExecutor = medicoRepository.findByUsername(medicoLogadoUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Médico", "username", medicoLogadoUsername));

        Consulta consulta = new Consulta();
        // Mapear dados do DTO para a entidade
        consulta.setPaciente(paciente);
        consulta.setMedicoConsulta(medicoExecutor); // Assinatura automática do médico que fez a consulta

        // Se a data da consulta não for fornecida no DTO, usar a data/hora atual.
        // Se o DTO já tiver dataConsulta (ex: o médico informou), usar essa.
        consulta.setDataConsulta(consultaDTO.getDataConsulta() != null ? consultaDTO.getDataConsulta() : LocalDateTime.now());

        consulta.setHistoriaMedica(consultaDTO.getHistoriaMedica());
        consulta.setSintomasAtuais(consultaDTO.getSintomasAtuais());
        consulta.setResultadosExames(consultaDTO.getResultadosExames());
        consulta.setInformacoesSituacaoClinica(consultaDTO.getInformacoesSituacaoClinica());
        consulta.setMedicacaoEmUso(consultaDTO.getMedicacaoEmUso());
        consulta.setAlergias(consultaDTO.getAlergias());
        consulta.setHistoricoVacinacao(consultaDTO.getHistoricoVacinacao());
        consulta.setSinaisVitais(consultaDTO.getSinaisVitais());
        consulta.setDadosImagem(consultaDTO.getDadosImagem());
        consulta.setDadosGeneticos(consultaDTO.getDadosGeneticos());
       
        return consultaRepository.save(consulta);
    }

  
    @Transactional(readOnly = true)
    public List<Consulta> buscarConsultasPorPacienteId(Long pacienteId) {
        // Verifica se o paciente existe antes de buscar as consultas (opcional, mas boa prática)
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new ResourceNotFoundException("Paciente", "id", pacienteId);
        }
        return consultaRepository.findByPacienteIdOrderByDataConsultaDesc(pacienteId);
    }

   
    @Transactional(readOnly = true)
    public Consulta buscarConsultaPorId(Long consultaId) {
        return consultaRepository.findById(consultaId)
            .orElseThrow(() -> new ResourceNotFoundException("Consulta", "id", consultaId));
    }

    
}