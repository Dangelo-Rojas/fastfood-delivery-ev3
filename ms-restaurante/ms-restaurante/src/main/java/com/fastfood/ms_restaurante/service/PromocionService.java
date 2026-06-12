package com.fastfood.ms_restaurante.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_restaurante.DTO.PromocionDTO;
import com.fastfood.ms_restaurante.model.Promocion;
import com.fastfood.ms_restaurante.repository.PromocionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PromocionService {

    private static final Logger log = LoggerFactory.getLogger(PromocionService.class);

    @Autowired
    private PromocionRepository promocionRepository;

    public List<PromocionDTO> obtenerTodos() {
        log.info("Obteniendo todas las promociones");
        return promocionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PromocionDTO buscarPorId(Integer id) {
        log.info("Buscando promocion con ID: {}", id);
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con ID " + id + " no encontrada"));
        return convertirADTO(promocion);
    }

    public Promocion guardar(Promocion promocion) {
        log.info("Guardando nueva promocion: {}", promocion.getNombrePromocion());
        return promocionRepository.save(promocion);
    }

    public Promocion actualizar(Integer id, Promocion datos) {
        log.info("Actualizando promocion con ID: {}", id);
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con ID " + id + " no encontrada"));
        if (datos.getNombrePromocion() != null) promocion.setNombrePromocion(datos.getNombrePromocion());
        if (datos.getDescripcionPromocion() != null) promocion.setDescripcionPromocion(datos.getDescripcionPromocion());
        if (datos.getPrecioPromocion() != null) promocion.setPrecioPromocion(datos.getPrecioPromocion());
        if (datos.getIdCatalogo() != null) promocion.setIdCatalogo(datos.getIdCatalogo());
        return promocionRepository.save(promocion);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando promocion con ID: {}", id);
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con ID " + id + " no encontrada"));
        promocionRepository.delete(promocion);
        return "Promocion " + promocion.getNombrePromocion() + " eliminada exitosamente.";
    }

    private PromocionDTO convertirADTO(Promocion promocion) {
        PromocionDTO dto = new PromocionDTO();
        dto.setIdPromocion(promocion.getIdPromocion());
        dto.setNombrePromocion(promocion.getNombrePromocion());
        dto.setDescripcionPromocion(promocion.getDescripcionPromocion());
        dto.setPrecioPromocion(promocion.getPrecioPromocion());
        dto.setIdCatalogo(promocion.getIdCatalogo());
        return dto;
    }
}