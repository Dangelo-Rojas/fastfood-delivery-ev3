package com.fastfood.ms_usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_usuario.DTO.RegionDTO;
import com.fastfood.ms_usuario.model.Region;
import com.fastfood.ms_usuario.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

    private static final Logger log = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodas() {
        log.info("Obteniendo todas las regiones");
        return regionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RegionDTO buscarPorId(Long id) {
        log.info("Buscando region con ID: {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region con ID " + id + " no encontrada"));
        return convertirADTO(region);
    }

    public Region guardar(Region region) {
        log.info("Guardando nueva region: {}", region.getNombre());
        return regionRepository.save(region);
    }

    public Region actualizar(Long id, Region datos) {
        log.info("Actualizando region con ID: {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region con ID " + id + " no encontrada"));
        region.setNombre(datos.getNombre());
        return regionRepository.save(region);
    }

    public String eliminar(Long id) {
        log.info("Eliminando region con ID: {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region con ID " + id + " no encontrada"));
        regionRepository.delete(region);
        return "Region " + region.getNombre() + " eliminada exitosamente.";
    }

    private RegionDTO convertirADTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setIdRegion(region.getIdRegion());
        dto.setNombre(region.getNombre());
        return dto;
    }
}