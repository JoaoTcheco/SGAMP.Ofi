package com.engsoft.sgamp.repository;

import com.engsoft.sgamp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Usuario.
 * Fornece operações CRUD e consultas personalizadas.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo nome de usuário (username).
     *
     * @param username Nome de usuário
     * @return Optional contendo o usuário, se encontrado
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica se já existe um usuário com o username fornecido.
     *
     * @param username Nome de usuário
     * @return true se existir, false caso contrário
     */
    boolean existsByUsername(String username);
}
