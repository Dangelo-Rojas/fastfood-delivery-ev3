package com.fastfood.ms_orden.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fastfood.ms_orden.DTO.CatalogoResponseDTO;

/**
 * Cliente REST para comunicarse con ms-restaurante.
 * Encapsula las llamadas HTTP usando WebClient.
 */
@Component
public class RestauranteClient {

    private static final Logger log = LoggerFactory.getLogger(RestauranteClient.class);

    @Autowired
    @Qualifier("restauranteWebClient")
    private WebClient restauranteWebClient;

    /**
     * Consulta un producto del catalogo en ms-restaurante.
     *
     * @param idCatalogo ID del producto a buscar
     * @return DTO con la info del producto
     * @throws RuntimeException si el producto no existe o ms-restaurante no responde
     */
    public CatalogoResponseDTO obtenerCatalogoPorId(Integer idCatalogo) {
        log.info("Consultando ms-restaurante - catalogo ID: {}", idCatalogo);

        try {
            CatalogoResponseDTO catalogo = restauranteWebClient.get()
                    .uri("/api/v1/catalogos/{id}", idCatalogo)
                    .retrieve()
                    .bodyToMono(CatalogoResponseDTO.class)
                    .block();

            log.info("Catalogo encontrado: {}", catalogo != null ? catalogo.getNombreCatalogo() : "null");
            return catalogo;

        } catch (WebClientResponseException.NotFound e) {
            log.error("Catalogo con ID {} no encontrado en ms-restaurante", idCatalogo);
            throw new RuntimeException("Catalogo con ID " + idCatalogo + " no encontrado en ms-restaurante");

        } catch (Exception e) {
            log.error("Error consultando ms-restaurante: {}", e.getMessage());
            throw new RuntimeException("Error al consultar ms-restaurante: " + e.getMessage());
        }
    }
}