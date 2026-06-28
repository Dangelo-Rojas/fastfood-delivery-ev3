package com.fastfood.ms_orden.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_orden.DTO.CarritoItemDTO;
import com.fastfood.ms_orden.DTO.CatalogoResponseDTO;
import com.fastfood.ms_orden.client.RestauranteClient;
import com.fastfood.ms_orden.model.CarritoItem;
import com.fastfood.ms_orden.repository.CarritoItemRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoItemService {

    private static final Logger log = LoggerFactory.getLogger(CarritoItemService.class);

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private RestauranteClient restauranteClient;

    public List<CarritoItemDTO> obtenerTodos() {
        log.info("Obteniendo todos los items");
        return carritoItemRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<CarritoItemDTO> obtenerPorCarrito(Integer idCarrito) {
        log.info("Obteniendo items del carrito con ID: {}", idCarrito);
        return carritoItemRepository.findByIdCarrito(idCarrito).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CarritoItemDTO buscarPorId(Integer id) {
        log.info("Buscando item con ID: {}", id);
        CarritoItem item = carritoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item con ID " + id + " no encontrado"));
        return convertirADTO(item);
    }

    /**
     * Guarda un nuevo item validando previamente que el catalogo exista
     * en ms-restaurante via WebClient (comunicacion REST entre microservicios).
    */
    public CarritoItemDTO guardar(CarritoItem item) {
        log.info("Guardando nuevo item - validando catalogo {} en ms-restaurante", item.getIdCatalogo());

        // Validar que el catalogo existe llamando a ms-restaurante
        CatalogoResponseDTO catalogo = restauranteClient.obtenerCatalogoPorId(item.getIdCatalogo());

        // Si el catalogo trae precio, lo usamos como precio unitario por defecto
        if (item.getPrecioUnitario() == null && catalogo.getPrecio() != null) {
            item.setPrecioUnitario(catalogo.getPrecio());
        }

        log.info("Catalogo '{}' validado - guardando item", catalogo.getNombreCatalogo());
        return convertirADTO(carritoItemRepository.save(item));
    }

    public CarritoItemDTO actualizar(Integer id, CarritoItem datos) {
        log.info("Actualizando item con ID: {}", id);
        CarritoItem item = carritoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item con ID " + id + " no encontrado"));
        if (datos.getCantidad() != null) item.setCantidad(datos.getCantidad());
        if (datos.getPrecioUnitario() != null) item.setPrecioUnitario(datos.getPrecioUnitario());
        if (datos.getIdCarrito() != null) item.setIdCarrito(datos.getIdCarrito());
        if (datos.getIdCatalogo() != null) item.setIdCatalogo(datos.getIdCatalogo());
        return convertirADTO(carritoItemRepository.save(item));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando item con ID: {}", id);
        CarritoItem item = carritoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item con ID " + id + " no encontrado"));
        carritoItemRepository.delete(item);
        return "Item con ID " + id + " eliminado exitosamente.";
    }

    private CarritoItemDTO convertirADTO(CarritoItem item) {
        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setIdCarritoItem(item.getIdCarritoItem());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setIdCarrito(item.getIdCarrito());
        dto.setIdCatalogo(item.getIdCatalogo());
        return dto;
    }
}