package br.com.fiap.soat07.hackathon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "Processador de vídeo API",
		version = "1.0",
		description = "Sistema de extração de frames de videos"))
@SpringBootApplication
public class ProcessadorDeVideosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessadorDeVideosApplication.class, args);
	}

}
