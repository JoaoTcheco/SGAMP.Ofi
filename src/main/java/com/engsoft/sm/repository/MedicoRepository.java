package com.engsoft.sm.repository;

import com.engsoft.sm.entity.Medico; 
import org.springframework.data.jpa.repository.JpaRepository; // Interface base do Spring Data JPA
import org.springframework.stereotype.Repository; 
import java.util.Optional; // Para retornos que podem ser nulos de forma segura


@Repository // Indica ao Spring que esta é uma interface de repositório gerenciada
public interface MedicoRepository extends JpaRepository<Medico, Long> { // <Entidade, TipoDaChavePrimaria>

    Optional<Medico> findByUsername(String username);

    
}