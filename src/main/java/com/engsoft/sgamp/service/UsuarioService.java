package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Retorna todos os usuários
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Busca um usuário por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Salva ou atualiza um usuário
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Remove um usuário pelo ID
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Busca um usuário pelo username
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}
