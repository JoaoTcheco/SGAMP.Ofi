package com.engsoft.sm.repository;

import com.engsoft.sm.entity.Medico; // Importa a entidade Medico
import org.springframework.data.jpa.repository.JpaRepository; // Interface base do Spring Data JPA
import org.springframework.stereotype.Repository; // Anotação para marcar como um componente de repositório Spring
import java.util.Optional; // Para retornos que podem ser nulos de forma segura


@Repository // Indica ao Spring que esta é uma interface de repositório gerenciada
public interface MedicoRepository extends JpaRepository<Medico, Long> { // <Entidade, TipoDaChavePrimaria>

    /**
     * Busca um médico pelo seu nome de usuário (username).
     * O Spring Data JPA automaticamente implementa este método baseado no nome.
     *
     * @param username O nome de usuário a ser buscado.
     * @return Um Optional contendo o Medico se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Medico> findByUsername(String username);

    
}