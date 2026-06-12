package com.fastfood.ms_restaurante.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_restaurante.DTO.CatalogoDTO;
import com.fastfood.ms_restaurante.model.Catalogo;
import com.fastfood.ms_restaurante.repository.CatalogoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CatalogoService {

    private static final Logger log = LoggerFactory.getLogger(CatalogoService.class);

    @Autowired
    private CatalogoRepository catalogoRepository;

    public List<CatalogoDTO> obtenerTodos() {
        log.info("Obteniendo todos los catalogos");
        return catalogoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CatalogoDTO buscarPorId(Integer id) {
        log.info("Buscando catalogo con ID: {}", id);
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalogo con ID " + id + " no encontrado"));
        return convertirADTO(catalogo);
    }

    public CatalogoDTO guardar(Catalogo catalogo) {
        log.info("Guardando nuevo catalogo: {}", catalogo.getNombreCatalogo());
        return convertirADTO(catalogoRepository.save(catalogo));
    }

    public CatalogoDTO actualizar(Integer id, Catalogo datos) {
        log.info("Actualizando catalogo con ID: {}", id);
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalogo con ID " + id + " no encontrado"));
        if (datos.getNombreCatalogo() != null) catalogo.setNombreCatalogo(datos.getNombreCatalogo());
        if (datos.getDescripcionCatalogo() != null) catalogo.setDescripcionCatalogo(datos.getDescripcionCatalogo());
        if (datos.getPrecio() != null) catalogo.setPrecio(datos.getPrecio());
        if (datos.getCategoria() != null) catalogo.setCategoria(datos.getCategoria());
        if (datos.getDisponible() != null) catalogo.setDisponible(datos.getDisponible());
        if (datos.getIdRestaurante() != null) catalogo.setIdRestaurante(datos.getIdRestaurante());
        return convertirADTO(catalogoRepository.save(catalogo));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando catalogo con ID: {}", id);
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalogo con ID " + id + " no encontrado"));
        catalogoRepository.delete(catalogo);
        return "Catalogo " + catalogo.getNombreCatalogo() + " eliminado exitosamente.";
    }

    private CatalogoDTO convertirADTO(Catalogo catalogo) {
        CatalogoDTO dto = new CatalogoDTO();
        dto.setIdCatalogo(catalogo.getIdCatalogo());
        dto.setNombreCatalogo(catalogo.getNombreCatalogo());
        dto.setDescripcionCatalogo(catalogo.getDescripcionCatalogo());
        dto.setPrecio(catalogo.getPrecio());
        dto.setCategoria(catalogo.getCategoria());
        dto.setDisponible(catalogo.getDisponible());
        dto.setIdRestaurante(catalogo.getIdRestaurante());
        return dto;
    }
}