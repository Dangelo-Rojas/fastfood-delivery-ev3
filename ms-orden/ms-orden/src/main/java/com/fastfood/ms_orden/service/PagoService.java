package com.fastfood.ms_orden.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_orden.DTO.PagoDTO;
import com.fastfood.ms_orden.model.Pago;
import com.fastfood.ms_orden.repository.PagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;

    public List<PagoDTO> obtenerTodos() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<PagoDTO> obtenerPorOrden(Long idOrden) {
        log.info("Obteniendo pagos de la orden con ID: {}", idOrden);
        return pagoRepository.findByIdOrden(idOrden).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PagoDTO buscarPorId(Long id) {
        log.info("Buscando pago con ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago con ID " + id + " no encontrado"));
        return convertirADTO(pago);
    }

    public PagoDTO guardar(Pago pago) {
        log.info("Guardando nuevo pago");
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("APROBADO");
        return convertirADTO(pagoRepository.save(pago));
    }

    public PagoDTO cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado de pago ID: {} a {}", id, nuevoEstado);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago con ID " + id + " no encontrado"));
        pago.setEstado(nuevoEstado);
        return convertirADTO(pagoRepository.save(pago));
    }

    public String eliminar(Long id) {
        log.info("Eliminando pago con ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago con ID " + id + " no encontrado"));
        pagoRepository.delete(pago);
        return "Pago eliminado exitosamente.";
    }

    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setIdOrden(pago.getIdOrden());
        dto.setIdMetodoPago(pago.getIdMetodoPago());
        dto.setMonto(pago.getMonto());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}