package com.fastfood.ms_orden.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_orden.DTO.OrdenDTO;
import com.fastfood.ms_orden.model.Orden;
import com.fastfood.ms_orden.repository.OrdenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenService {

    private static final Logger log = LoggerFactory.getLogger(OrdenService.class);

    @Autowired
    private OrdenRepository ordenRepository;

    public List<OrdenDTO> obtenerTodas() {
        log.info("Obteniendo todas las ordenes");
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<OrdenDTO> obtenerPorEstado(String estado) {
        log.info("Obteniendo ordenes con estado: {}", estado);
        return ordenRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public OrdenDTO buscarPorId(Long id) {
        log.info("Buscando orden con ID: {}", id);
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Orden no encontrada con ID: {}", id);
                    return new RuntimeException("Orden no encontrada con ID: " + id);
                });
        return convertirADTO(orden);
    }

    public OrdenDTO buscarPorCarrito(Integer idCarrito) {
        log.info("Buscando orden del carrito con ID: {}", idCarrito);
        Orden orden = ordenRepository.findByIdCarrito(idCarrito);
        if (orden == null) {
            log.error("No existe orden para el carrito con ID: {}", idCarrito);
            throw new RuntimeException("No existe orden para el carrito con ID: " + idCarrito);
        }
        return convertirADTO(orden);
    }

    public OrdenDTO guardar(Orden orden) {
        log.info("Creando nueva orden para carrito ID: {}", orden.getIdCarrito());
        orden.setFechaOrden(LocalDateTime.now());
        orden.setTotal(orden.getSubtotal() - orden.getDescuento());
        OrdenDTO guardada = convertirADTO(ordenRepository.save(orden));
        log.info("Orden creada con ID: {}", guardada.getIdOrden());
        return guardada;
    }

    public OrdenDTO cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado de orden ID: {} a {}", id, nuevoEstado);
        if (!nuevoEstado.equals("PENDIENTE") && !nuevoEstado.equals("EN_PROCESO") &&
            !nuevoEstado.equals("EN_CAMINO") && !nuevoEstado.equals("ENTREGADO") &&
            !nuevoEstado.equals("CANCELADO")) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Orden no encontrada con ID: {}", id);
                    return new RuntimeException("Orden no encontrada con ID: " + id);
                });
        orden.setEstado(nuevoEstado);
        log.info("Estado de orden ID: {} cambiado a {}", id, nuevoEstado);
        return convertirADTO(ordenRepository.save(orden));
    }

    public String eliminar(Long id) {
        log.info("Eliminando orden con ID: {}", id);
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Orden no encontrada con ID: {}", id);
                    return new RuntimeException("Orden no encontrada con ID: " + id);
                });
        ordenRepository.delete(orden);
        log.info("Orden con ID: {} eliminada exitosamente", id);
        return "Orden con ID " + id + " eliminada exitosamente.";
    }

    private OrdenDTO convertirADTO(Orden orden) {
        OrdenDTO dto = new OrdenDTO();
        dto.setIdOrden(orden.getIdOrden());
        dto.setFechaOrden(orden.getFechaOrden());
        dto.setEstado(orden.getEstado());
        dto.setSubtotal(orden.getSubtotal());
        dto.setDescuento(orden.getDescuento());
        dto.setTotal(orden.getTotal());
        dto.setIdCarrito(orden.getIdCarrito());
        return dto;
    }
}
