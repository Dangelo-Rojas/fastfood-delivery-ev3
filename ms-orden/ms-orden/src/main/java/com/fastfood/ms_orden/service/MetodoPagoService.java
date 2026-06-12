package com.fastfood.ms_orden.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_orden.DTO.MetodoPagoDTO;
import com.fastfood.ms_orden.model.MetodoPago;
import com.fastfood.ms_orden.repository.MetodoPagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetodoPagoService {

    private static final Logger log = LoggerFactory.getLogger(MetodoPagoService.class);

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public List<MetodoPagoDTO> obtenerTodos() {
        log.info("Obteniendo todos los metodos de pago");
        return metodoPagoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public MetodoPagoDTO buscarPorId(Long id) {
        log.info("Buscando metodo de pago con ID: {}", id);
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metodo de pago con ID " + id + " no encontrado"));
        return convertirADTO(metodoPago);
    }

    public MetodoPagoDTO guardar(MetodoPago metodoPago) {
        log.info("Guardando nuevo metodo de pago: {}", metodoPago.getNombre());
        return convertirADTO(metodoPagoRepository.save(metodoPago));
    }

    public MetodoPagoDTO actualizar(Long id, MetodoPago datos) {
        log.info("Actualizando metodo de pago con ID: {}", id);
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metodo de pago con ID " + id + " no encontrado"));
        metodoPago.setNombre(datos.getNombre());
        return convertirADTO(metodoPagoRepository.save(metodoPago));
    }

    public String eliminar(Long id) {
        log.info("Eliminando metodo de pago con ID: {}", id);
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metodo de pago con ID " + id + " no encontrado"));
        metodoPagoRepository.delete(metodoPago);
        return "Metodo de pago " + metodoPago.getNombre() + " eliminado exitosamente.";
    }

    private MetodoPagoDTO convertirADTO(MetodoPago metodoPago) {
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setIdMetodoPago(metodoPago.getIdMetodoPago());
        dto.setNombre(metodoPago.getNombre());
        return dto;
    }
}