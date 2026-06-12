package com.fastfood.ms_orden.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_orden.DTO.CarritoDTO;
import com.fastfood.ms_orden.model.Carrito;
import com.fastfood.ms_orden.repository.CarritoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    @Autowired
    private CarritoRepository carritoRepository;

    public List<CarritoDTO> obtenerTodos() {
        log.info("Obteniendo todos los carritos");
        return carritoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CarritoDTO buscarPorId(Integer id) {
        log.info("Buscando carrito con ID: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito con ID " + id + " no encontrado"));
        return convertirADTO(carrito);
    }

    public CarritoDTO guardar(Carrito carrito) {
        log.info("Guardando nuevo carrito");
        return convertirADTO(carritoRepository.save(carrito));
    }

    public CarritoDTO actualizar(Integer id, Carrito datos) {
        log.info("Actualizando carrito con ID: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito con ID " + id + " no encontrado"));
        if (datos.getEstado() != null) carrito.setEstado(datos.getEstado());
        if (datos.getIdUsuario() != null) carrito.setIdUsuario(datos.getIdUsuario());
        if (datos.getIdDireccion() != null) carrito.setIdDireccion(datos.getIdDireccion());
        return convertirADTO(carritoRepository.save(carrito));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando carrito con ID: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito con ID " + id + " no encontrado"));
        carritoRepository.delete(carrito);
        return "Carrito con ID " + id + " eliminado exitosamente.";
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setIdCarrito(carrito.getIdCarrito());
        dto.setEstado(carrito.getEstado());
        dto.setIdUsuario(carrito.getIdUsuario());
        dto.setIdDireccion(carrito.getIdDireccion());
        return dto;
    }
}