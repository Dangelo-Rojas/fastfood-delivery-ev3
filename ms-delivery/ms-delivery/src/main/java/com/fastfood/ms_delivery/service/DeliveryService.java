package com.fastfood.ms_delivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_delivery.DTO.DeliveryDTO;
import com.fastfood.ms_delivery.model.Delivery;
import com.fastfood.ms_delivery.repository.DeliveryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<DeliveryDTO> obtenerTodos() {
        log.info("Obteniendo todos los deliveries");
        return deliveryRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<DeliveryDTO> obtenerPorEstado(String estado) {
        log.info("Obteniendo deliveries con estado: {}", estado);
        return deliveryRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<DeliveryDTO> obtenerPorConductor(Long idConductor) {
        log.info("Obteniendo deliveries del conductor ID: {}", idConductor);
        return deliveryRepository.findByIdConductor(idConductor).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public DeliveryDTO buscarPorId(Long id) {
        log.info("Buscando delivery con ID: {}", id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delivery no encontrado con ID: {}", id);
                    return new RuntimeException("Delivery no encontrado con ID: " + id);
                });
        return convertirADTO(delivery);
    }

    public DeliveryDTO buscarPorOrden(Long idOrden) {
        log.info("Buscando delivery de la orden ID: {}", idOrden);
        Delivery delivery = deliveryRepository.findByIdOrden(idOrden);
        if (delivery == null) {
            log.error("No existe delivery para la orden con ID: {}", idOrden);
            throw new RuntimeException("No existe delivery para la orden con ID: " + idOrden);
        }
        return convertirADTO(delivery);
    }

    public DeliveryDTO guardar(Delivery delivery) {
        log.info("Creando nuevo delivery para orden ID: {}", delivery.getIdOrden());
        delivery.setHoraSalida(LocalDateTime.now());
        DeliveryDTO guardado = convertirADTO(deliveryRepository.save(delivery));
        log.info("Delivery creado con ID: {}", guardado.getIdDelivery());
        return guardado;
    }

    public DeliveryDTO cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado de delivery ID: {} a {}", id, nuevoEstado);
        if (!nuevoEstado.equals("PENDIENTE") && !nuevoEstado.equals("EN_CAMINO") &&
            !nuevoEstado.equals("ENTREGADO") && !nuevoEstado.equals("CANCELADO")) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delivery no encontrado con ID: {}", id);
                    return new RuntimeException("Delivery no encontrado con ID: " + id);
                });
        if (nuevoEstado.equals("ENTREGADO")) {
            delivery.setHoraLlegada(LocalDateTime.now());
        }
        delivery.setEstado(nuevoEstado);
        log.info("Estado de delivery ID: {} cambiado a {}", id, nuevoEstado);
        return convertirADTO(deliveryRepository.save(delivery));
    }

    public String eliminar(Long id) {
        log.info("Eliminando delivery con ID: {}", id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delivery no encontrado con ID: {}", id);
                    return new RuntimeException("Delivery no encontrado con ID: " + id);
                });
        deliveryRepository.delete(delivery);
        log.info("Delivery con ID: {} eliminado exitosamente", id);
        return "Delivery con ID " + id + " eliminado exitosamente.";
    }

    private DeliveryDTO convertirADTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setIdDelivery(delivery.getIdDelivery());
        dto.setHoraSalida(delivery.getHoraSalida());
        dto.setHoraLlegada(delivery.getHoraLlegada());
        dto.setEstado(delivery.getEstado());
        dto.setIdOrden(delivery.getIdOrden());
        dto.setIdConductor(delivery.getIdConductor());
        return dto;
    }
}