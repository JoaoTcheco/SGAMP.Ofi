package com.engsoft.sgamp.service;

import com.engsoft.sgamp.model.Usuario;
import com.engsoft.sgamp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public boolean usernameDisponivel(String username) {
        return !usuarioRepository.existsByUsername(username);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }
}
