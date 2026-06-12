package com.fastfood.ms_usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_usuario.DTO.ComunaDTO;
import com.fastfood.ms_usuario.model.Comuna;
import com.fastfood.ms_usuario.repository.ComunaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {

    private static final Logger log = LoggerFactory.getLogger(ComunaService.class);

    @Autowired
    private ComunaRepository comunaRepository;

    public List<ComunaDTO> obtenerTodas() {
        log.info("Obteniendo todas las comunas");
        return comunaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ComunaDTO buscarPorId(Long id) {
        log.info("Buscando comuna con ID: {}", id);
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna con ID " + id + " no encontrada"));
        return convertirADTO(comuna);
    }

    public Comuna guardar(Comuna comuna) {
        log.info("Guardando nueva comuna: {}", comuna.getNombre());
        return comunaRepository.save(comuna);
    }

    public Comuna actualizar(Long id, Comuna datos) {
        log.info("Actualizando comuna con ID: {}", id);
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna con ID " + id + " no encontrada"));
        comuna.setNombre(datos.getNombre());
        if (datos.getIdRegion() != null) comuna.setIdRegion(datos.getIdRegion());
        return comunaRepository.save(comuna);
    }

    public String eliminar(Long id) {
        log.info("Eliminando comuna con ID: {}", id);
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna con ID " + id + " no encontrada"));
        comunaRepository.delete(comuna);
        return "Comuna " + comuna.getNombre() + " eliminada exitosamente.";
    }

    private ComunaDTO convertirADTO(Comuna comuna) {
        ComunaDTO dto = new ComunaDTO();
        dto.setIdComuna(comuna.getIdComuna());
        dto.setNombre(comuna.getNombre());
        dto.setIdRegion(comuna.getIdRegion());
        return dto;
    }
}