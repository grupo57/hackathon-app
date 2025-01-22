package br.com.fiap.soat07.hackathon.infra.repository.mysql.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransacionalConfig {
    // Configurações do DataSource e EntityManager aqui
}