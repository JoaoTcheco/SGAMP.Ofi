package com.engsoft.sm.repository;

import com.engsoft.sm.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Para consultas dinâmicas com Criteria API
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade {@link Paciente}.
 * Além dos métodos CRUD básicos, também estende JpaSpecificationExecutor
 * para permitir a construção de consultas dinâmicas (filtros complexos).
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {

    /**
     * Busca pacientes cujo nome completo contenha a string fornecida, ignorando maiúsculas/minúsculas.
     *
     * @param nome trecho do nome a ser buscado.
     * @return Uma lista de pacientes que correspondem ao critério.
     */
    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);

    /**
     * Busca um paciente pelo seu Número de Identificação Fiscal (NIF).
     *
     * @param nif O NIF a ser buscado.
     * @return Um Optional contendo o Paciente se encontrado.
     */
    Optional<Paciente> findByNif(String nif);

    /**
     * Busca um paciente pelo seu Número de Utente do SNS.
     *
     * @param numeroUtenteSNS O número de utente do SNS a ser buscado.
     * @return Um Optional contendo o Paciente se encontrado.
     */
    Optional<Paciente> findByNumeroUtenteSNS(String numeroUtenteSNS);

    // Para a funcionalidade de "listar pacientes por várias condições",
    // usaremos JpaSpecificationExecutor, que permite criar queries dinâmicas
    // na camada de serviço usando a Criteria API do JPA.
    // Não precisamos definir métodos específicos aqui para cada combinação de filtro,
    // a menos que sejam consultas muito comuns e fixas.
}