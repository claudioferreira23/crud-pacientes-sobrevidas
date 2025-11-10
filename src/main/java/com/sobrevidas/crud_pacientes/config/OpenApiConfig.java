package com.sobrevidas.crud_pacientes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Pacientes",
                version = "v1.0",
                description = "API REST para gerenciamento de pacientes."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desenvolvimento Local"
                )
        }
)
public class OpenApiConfig {
}
