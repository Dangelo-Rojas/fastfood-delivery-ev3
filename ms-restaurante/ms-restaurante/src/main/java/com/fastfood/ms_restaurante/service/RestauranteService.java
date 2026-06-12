package com.fastfood.ms_restaurante.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_restaurante.DTO.RestauranteDTO;
import com.fastfood.ms_restaurante.model.Restaurante;
import com.fastfood.ms_restaurante.repository.RestauranteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RestauranteService {

    private static final Logger log = LoggerFactory.getLogger(RestauranteService.class);

    @Autowired
    private RestauranteRepository restauranteRepository;

    public List<RestauranteDTO> obtenerTodos() {
        log.info("Obteniendo todos los restaurantes");
        return restauranteRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RestauranteDTO buscarPorId(Integer id) {
        log.info("Buscando restaurante con ID: {}", id);
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Restaurante no encontrado con ID: {}", id);
                    return new RuntimeException("Restaurante no encontrado");
                });
        return convertirADTO(restaurante);
    }

    public Restaurante guardarRestaurante(Restaurante restaurante) {
        log.info("Guardando nuevo restaurante: {}", restaurante.getNombre_restaurante());
        Restaurante guardado = restauranteRepository.save(restaurante);
        log.info("Restaurante guardado con ID: {}", guardado.getId_restaurante());
        return guardado;
    }

    public Restaurante actualizarRestaurante(Integer id, Restaurante restaurante) {
        log.info("Actualizando restaurante con ID: {}", id);
        Restaurante restaurant = restauranteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Restaurante no encontrado con ID: {}", id);
                    return new RuntimeException("El restaurante no existe en nuestros registros.");
                });
        if (restaurante.getNombre_restaurante() != null) restaurant.setNombre_restaurante(restaurante.getNombre_restaurante());
        if (restaurante.getDireccion_restaurante() != null) restaurant.setDireccion_restaurante(restaurante.getDireccion_restaurante());
        if (restaurante.getTelefono_restaurante() != null) restaurant.setTelefono_restaurante(restaurante.getTelefono_restaurante());
        if (restaurante.getCorreo_restaurante() != null) restaurant.setCorreo_restaurante(restaurante.getCorreo_restaurante());
        log.info("Restaurante con ID: {} actualizado exitosamente", id);
        return restauranteRepository.save(restaurant);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando restaurante con ID: {}", id);
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Restaurante no encontrado con ID: {}", id);
                    return new RuntimeException("El restaurante con ID " + id + " no existe.");
                });
        restauranteRepository.delete(restaurante);
        log.info("Restaurante {} eliminado exitosamente", restaurante.getNombre_restaurante());
        return "El restaurante '" + restaurante.getNombre_restaurante() + "' ha sido eliminado correctamente.";
    }

    private RestauranteDTO convertirADTO(Restaurante restaurante) {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setId_restaurante(restaurante.getId_restaurante());
        dto.setNombre_restaurante(restaurante.getNombre_restaurante());
        dto.setDireccion_restaurante(restaurante.getDireccion_restaurante());
        dto.setTelefono_restaurante(restaurante.getTelefono_restaurante());
        dto.setCorreo_restaurante(restaurante.getCorreo_restaurante());
        return dto;
    }
}