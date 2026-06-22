package com.fastfood.ms_restaurante.service;

import com.fastfood.ms_restaurante.DTO.CatalogoDTO;
import com.fastfood.ms_restaurante.model.Catalogo;
import com.fastfood.ms_restaurante.repository.CatalogoRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - CatalogoService")
class CatalogoServiceTest {

    @Mock
    private CatalogoRepository catalogoRepository;

    @InjectMocks
    private CatalogoService catalogoService;

    private Catalogo catalogo;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor → new Catalogo(id, nombre, descripcion, precio, categoria, disponible, idRestaurante)
        catalogo = new Catalogo(1, "Hamburguesa Clásica", "Con queso, lechuga y tomate", 5990.0, "Hamburguesas", true, 1);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos: retorna lista mapeada a DTOs con todos los campos")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        // Given
        Catalogo c2 = new Catalogo(2, "Papas Fritas", "Crujientes y doradas", 2990.0, "Acompañamientos", true, 1);
        when(catalogoRepository.findAll()).thenReturn(Arrays.asList(catalogo, c2));

        // When
        List<CatalogoDTO> resultado = catalogoService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreCatalogo()).isEqualTo("Hamburguesa Clásica");
        assertThat(resultado.get(1).getNombreCatalogo()).isEqualTo("Papas Fritas");
        verify(catalogoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacía cuando no hay catálogos")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        // Given
        when(catalogoRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CatalogoDTO> resultado = catalogoService.obtenerTodos();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO con todos los campos mapeados cuando el catálogo existe")
    void buscarPorId_existente_retornaDTOCompleto() {
        // Given
        when(catalogoRepository.findById(1)).thenReturn(Optional.of(catalogo));

        // When
        CatalogoDTO resultado = catalogoService.buscarPorId(1);

        // Then
        assertThat(resultado.getIdCatalogo()).isEqualTo(1);
        assertThat(resultado.getNombreCatalogo()).isEqualTo("Hamburguesa Clásica");
        assertThat(resultado.getPrecio()).isEqualTo(5990.0);
        assertThat(resultado.getCategoria()).isEqualTo("Hamburguesas");
        assertThat(resultado.getDisponible()).isTrue();
        assertThat(resultado.getIdRestaurante()).isEqualTo(1);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con mensaje 'Catalogo con ID X no encontrado' cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConMensaje() {
        // Given
        when(catalogoRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> catalogoService.buscarPorId(99));
        assertThat(ex.getMessage()).isEqualTo("Catalogo con ID 99 no encontrado");
    }

    // ── guardar ───────────────────────────────────────────────────────

    @Test
    @DisplayName("guardar: persiste y retorna DTO con los datos del catálogo guardado")
    void guardar_datosValidos_retornaDTOPersistido() {
        // Given
        when(catalogoRepository.save(catalogo)).thenReturn(catalogo);

        // When
        CatalogoDTO resultado = catalogoService.guardar(catalogo);

        // Then
        assertThat(resultado.getIdCatalogo()).isEqualTo(1);
        assertThat(resultado.getNombreCatalogo()).isEqualTo("Hamburguesa Clásica");
        assertThat(resultado.getIdRestaurante()).isEqualTo(1);
        verify(catalogoRepository, times(1)).save(catalogo);
    }

    // ── actualizar ────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizar: aplica solo los campos no nulos sobre el catálogo existente")
    void actualizar_camposParcialesNoNulos_actualizaYGuarda() {
        // Given
        Catalogo datos = new Catalogo(null, null, null, 6990.0, null, false, null);
        when(catalogoRepository.findById(1)).thenReturn(Optional.of(catalogo));
        when(catalogoRepository.save(catalogo)).thenReturn(catalogo);

        // When
        CatalogoDTO resultado = catalogoService.actualizar(1, datos);

        // Then
        assertThat(catalogo.getPrecio()).isEqualTo(6990.0);
        assertThat(catalogo.getDisponible()).isFalse();
        // nombre no cambió porque era null en datos
        assertThat(catalogo.getNombreCatalogo()).isEqualTo("Hamburguesa Clásica");
        verify(catalogoRepository, times(1)).save(catalogo);
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando el catálogo no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        // Given
        when(catalogoRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> catalogoService.actualizar(99, new Catalogo()));
        assertThat(ex.getMessage()).contains("99");
        verify(catalogoRepository, never()).save(any());
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina y retorna mensaje con el nombre del catálogo")
    void eliminar_existente_retornaMensajeConNombre() {
        // Given
        when(catalogoRepository.findById(1)).thenReturn(Optional.of(catalogo));
        doNothing().when(catalogoRepository).delete(catalogo);

        // When
        String resultado = catalogoService.eliminar(1);

        // Then
        assertThat(resultado).isEqualTo("Catalogo Hamburguesa Clásica eliminado exitosamente.");
        verify(catalogoRepository, times(1)).delete(catalogo);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException sin llamar a delete cuando no existe")
    void eliminar_noExistente_lanzaExcepcionSinEliminar() {
        // Given
        when(catalogoRepository.findById(55)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> catalogoService.eliminar(55));
        verify(catalogoRepository, never()).delete(any());
    }
}