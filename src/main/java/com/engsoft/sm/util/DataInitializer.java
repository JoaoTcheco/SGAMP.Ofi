package com.engsoft.sm.util; // Você pode criar este pacote se não existir

import com.engsoft.sm.entity.Medico;
import com.engsoft.sm.repository.MedicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final MedicoRepository medicoRepository;
    // private final PasswordEncoder passwordEncoder; // Removido pois a senha será em texto plano

    // public DataInitializer(MedicoRepository medicoRepository, PasswordEncoder passwordEncoder) { // Modificado
    public DataInitializer(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
        // this.passwordEncoder = passwordEncoder; // Removido
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Iniciando DataInitializer para verificar/criar médico de teste...");

        // Verificar se o médico de teste já existe
        if (medicoRepository.findByUsername("medicoteste").isEmpty()) {
            Medico medicoTeste = new Medico();
            medicoTeste.setUsername("medicoteste");
            // ATENÇÃO: Senha em texto plano. Use o PasswordEncoder se voltar para BCrypt.
            // medicoTeste.setSenha(passwordEncoder.encode("senha123"));
            medicoTeste.setSenha("senha123"); // Senha em texto plano
            medicoTeste.setNomeCompleto("Dr. Teste da Silva");
            medicoTeste.setAtivo(true);
            // Não é necessário setar pacientesCadastrados ou consultasRealizadas aqui para o login

            medicoRepository.save(medicoTeste);
            logger.info("Médico de teste 'medicoteste' criado com senha 'senha123'.");
        } else {
            logger.info("Médico de teste 'medicoteste' já existe.");
            // Opcional: verificar se a senha está correta para o modo NoOp
            Medico medicoExistente = medicoRepository.findByUsername("medicoteste").get();
            if (!medicoExistente.getSenha().equals("senha123")) {
                 logger.warn("A senha do médico 'medicoteste' no banco não é 'senha123'. " +
                             "Se estiver testando o login sem criptografia, atualize a senha no banco para 'senha123' ou ajuste este inicializador.");
            }
        }

        
    }
}

