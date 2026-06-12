package com.fastfood.ms_usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_usuario.DTO.DireccionDTO;
import com.fastfood.ms_usuario.model.Direccion;
import com.fastfood.ms_usuario.repository.DireccionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DireccionService {

    private static final Logger log = LoggerFactory.getLogger(DireccionService.class);

    @Autowired
    private DireccionRepository direccionRepository;

    public List<DireccionDTO> obtenerTodas() {
        log.info("Obteniendo todas las direcciones");
        return direccionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<DireccionDTO> obtenerPorUsuario(Long idUsuario) {
        log.info("Obteniendo direcciones del usuario con ID: {}", idUsuario);
        return direccionRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public DireccionDTO buscarPorId(Long id) {
        log.info("Buscando direccion con ID: {}", id);
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Direccion con ID " + id + " no encontrada"));
        return convertirADTO(direccion);
    }

    public DireccionDTO guardar(Direccion direccion) {
        log.info("Guardando nueva direccion");
        return convertirADTO(direccionRepository.save(direccion));
    }

    public Direccion actualizar(Long id, Direccion datos) {
        log.info("Actualizando direccion con ID: {}", id);
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Direccion con ID " + id + " no encontrada"));
        direccion.setCalle(datos.getCalle());
        direccion.setNumero(datos.getNumero());
        direccion.setDepto(datos.getDepto());
        direccion.setReferencia(datos.getReferencia());
        if (datos.getIdComuna() != null) direccion.setIdComuna(datos.getIdComuna());
        return direccionRepository.save(direccion);
    }

    public String eliminar(Long id) {
        log.info("Eliminando direccion con ID: {}", id);
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Direccion con ID " + id + " no encontrada"));
        direccionRepository.delete(direccion);
        return "Direccion eliminada exitosamente.";
    }

    private DireccionDTO convertirADTO(Direccion direccion) {
        DireccionDTO dto = new DireccionDTO();
        dto.setIdDireccion(direccion.getIdDireccion());
        dto.setCalle(direccion.getCalle());
        dto.setNumero(direccion.getNumero());
        dto.setDepto(direccion.getDepto());
        dto.setReferencia(direccion.getReferencia());
        dto.setIdComuna(direccion.getIdComuna());
        dto.setIdUsuario(direccion.getIdUsuario());
        return dto;
    }
}