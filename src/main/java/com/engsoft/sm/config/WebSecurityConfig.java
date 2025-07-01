package com.engsoft.sm.config;

import com.engsoft.sm.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder; // Importar NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração para o Spring Security.
 * Define como a autenticação e autorização são tratadas na aplicação.
 * ATENÇÃO: PasswordEncoder configurado para NoOpPasswordEncoder para TESTES. NÃO USE EM PRODUÇÃO.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    

    private final CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Define um bean para o PasswordEncoder.
     * Para TESTES, estamos usando NoOpPasswordEncoder, que não criptografa senhas.
     * NÃO USE EM PRODUÇÃO. Em produção, use BCryptPasswordEncoder.
     * @return uma instância de NoOpPasswordEncoder.
     */
    @SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder() {
        // ATENÇÃO: Usando NoOpPasswordEncoder APENAS PARA TESTES.
        // Em produção, volte para: return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configura a cadeia de filtros de segurança HTTP.
     * Define quais URLs são públicas, quais requerem autenticação,
     * a página de login, e o comportamento de logout.
     *
     * @param http O objeto HttpSecurity a ser configurado.
     * @return O SecurityFilterChain construído.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configura a autorização para as requisições HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso não autenticado a estas URLs (página de login, CSS, JS, imagens)
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**").permitAll() // Adicionado /h2-console/** se estiver usando
                // Qualquer outra requisição deve ser autenticada
                .anyRequest().authenticated()
            )
            // Configura o formulário de login
            .formLogin(form -> form
                .loginPage("/login") // URL da página de login customizada
                .loginProcessingUrl("/login") // URL para onde o formulário de login envia os dados
                .defaultSuccessUrl("/painel", true) // URL para redirecionar após login bem-sucedido
                .failureUrl("/login?error=true") // URL para redirecionar após falha no login
                .permitAll() // Permite acesso à página de login para todos
            )
            // Configura o logout
            .logout(logout -> logout
                .logoutUrl("/logout") // URL para acionar o logout
                .logoutSuccessUrl("/login?logout=true") // URL para redirecionar após logout bem-sucedido
                .invalidateHttpSession(true) // Invalida a sessão HTTP
                .deleteCookies("JSESSIONID") // Deleta cookies
                .permitAll() // Permite acesso à funcionalidade de logout para todos
            );
            
        // Se estiver usando o console H2, descomente e ajuste as linhas abaixo para desabilitar CSRF e frame options para o console
        // http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        // http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        // Para simplificar os testes, se não estiver usando H2 e quiser desabilitar CSRF (NÃO RECOMENDADO PARA PRODUÇÃO):
        http.csrf(csrf -> csrf.disable());//desabilitamos um tipo de criptografia de net ou 

        return http.build();
    }

    /**
     * Configura o AuthenticationManagerBuilder para usar o CustomUserDetailsService
     * e o PasswordEncoder definidos.
     *
     * @param auth O AuthenticationManagerBuilder a ser configurado.
     * @throws Exception Se ocorrer um erro.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(customUserDetailsService) // Define nosso serviço de detalhes do usuário
            .passwordEncoder(passwordEncoder()); // Define o codificador de senhas (NoOpPasswordEncoder para teste)
    }
}
