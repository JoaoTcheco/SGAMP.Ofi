package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);
    
    @Query("SELECT p FROM Paciente p WHERE " +
           "(:nome IS NULL OR p.nomeCompleto LIKE %:nome%) AND " +
           "(:idade IS NULL OR p.idade = :idade) AND " +
           "(:sexo IS NULL OR p.sexo = :sexo)")
    List<Paciente> findByFiltros(
            @Param("nome") String nome,
            @Param("idade") Integer idade,
            @Param("sexo") Paciente.Sexo sexo);
}