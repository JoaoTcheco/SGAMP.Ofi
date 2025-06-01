package com.engsoft.sm.service;

import com.engsoft.sm.entity.Medico; // Entidade Médico
import com.engsoft.sm.repository.MedicoRepository; // Repositório para buscar o médico

import org.springframework.security.core.GrantedAuthority; // Para permissões/roles
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Implementação de GrantedAuthority
import org.springframework.security.core.userdetails.User; // Classe User do Spring Security
import org.springframework.security.core.userdetails.UserDetails; // Interface UserDetails
import org.springframework.security.core.userdetails.UserDetailsService; // Interface que estamos implementando
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exceção padrão
import org.springframework.stereotype.Service; // Marca como um serviço Spring

import java.util.Collection;
import java.util.Collections; // Para criar uma lista imutável de permissões

/**
 * Serviço customizado para carregar detalhes do usuário (Médico) para o Spring Security.
 * Implementa a interface UserDetailsService para integrar com o mecanismo de autenticação.
 */
@Service // Define esta classe como um componente de serviço gerenciado pelo Spring
public class CustomUserDetailsService implements UserDetailsService {

    private final MedicoRepository medicoRepository;

    
    public CustomUserDetailsService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o médico no banco de dados pelo username
        Medico medico = medicoRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Médico não encontrado com o username: " + username));

        
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEDICO"));

        return new User(medico.getUsername(), medico.getSenha(), authorities);
    }
}