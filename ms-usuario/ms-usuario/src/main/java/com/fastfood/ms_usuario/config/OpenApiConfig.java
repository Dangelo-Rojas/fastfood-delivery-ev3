package com.fastfood.ms_usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-usuario API")
                        .version("1.0.0")
                        .description("Microservicio de gestión de usuarios, regiones, comunas y direcciones"));
    }
}
