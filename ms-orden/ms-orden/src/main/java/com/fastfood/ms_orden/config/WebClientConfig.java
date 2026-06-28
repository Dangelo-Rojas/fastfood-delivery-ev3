package com.fastfood.ms_orden.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.restaurante.url}")
    private String restauranteUrl;

    @Bean(name = "restauranteWebClient")
    public WebClient restauranteWebClient() {
        return WebClient.builder()
                .baseUrl(restauranteUrl)
                .build();
    }
}