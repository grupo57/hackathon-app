package br.com.fiap.soat07.hackathon.infra.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // URLs que não requerem autenticação
                        .requestMatchers(
                                "/swagger-ui/**",        // Permitir Swagger UI
                                "/swagger-ui.html",      // Permitir Swagger UI clássico (se necessário)
                                "/v3/**",                // Permitir o acesso aos docs da API
                                "/v3/api-docs/**",       // Permitir o acesso aos docs da API
                                "/webjars/**",           // Permitir acesso aos recursos estáticos do Swagger (como CSS e JS)
                                "/login",                // URLs de login também sem autenticação
                                "/login/**",             // URLs de login também sem autenticação
                                "/",                     // URL com informações da aplicação
                                "/grupo/**"              // URLs de informações do grupo também sem autenticação
                        ).permitAll()
                        .anyRequest().authenticated() // Qualquer outra requisição requer autenticação
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}