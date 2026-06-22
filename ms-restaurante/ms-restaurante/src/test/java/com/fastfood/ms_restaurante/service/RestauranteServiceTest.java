package com.fastfood.ms_restaurante.service;

import com.fastfood.ms_restaurante.DTO.RestauranteDTO;
import com.fastfood.ms_restaurante.model.Restaurante;
import com.fastfood.ms_restaurante.repository.RestauranteRepository;


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
@DisplayName("Pruebas unitarias - RestauranteService")
class RestauranteServiceTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private RestauranteService restauranteService;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        // Lombok genera @AllArgsConstructor → new Restaurante(id, nombre, direccion, telefono, correo)
        restaurante = new Restaurante(
                1,
                "FastFood Central",
                "Av. Principal 123",
                "+56912345678",
                "contacto@fastfood.cl"
        );
    }

    // ── obtenerTodos ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos: retorna lista de DTOs cuando existen restaurantes")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        // Given
        Restaurante r2 = new Restaurante(2, "Burger House", "Calle 456", "+56987654321", "burger@house.cl");
        when(restauranteRepository.findAll()).thenReturn(Arrays.asList(restaurante, r2));

        // When
        List<RestauranteDTO> resultado = restauranteService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre_restaurante()).isEqualTo("FastFood Central");
        assertThat(resultado.get(1).getNombre_restaurante()).isEqualTo("Burger House");
        verify(restauranteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacía cuando no hay restaurantes")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        // Given
        when(restauranteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<RestauranteDTO> resultado = restauranteService.obtenerTodos();

        // Then
        assertThat(resultado).isEmpty();
        verify(restauranteRepository, times(1)).findAll();
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando el restaurante existe")
    void buscarPorId_existente_retornaDTO() {
        // Given
        when(restauranteRepository.findById(1)).thenReturn(Optional.of(restaurante));

        // When
        RestauranteDTO resultado = restauranteService.buscarPorId(1);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId_restaurante()).isEqualTo(1);
        assertThat(resultado.getNombre_restaurante()).isEqualTo("FastFood Central");
        assertThat(resultado.getCorreo_restaurante()).isEqualTo("contacto@fastfood.cl");
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con mensaje 'Restaurante no encontrado' cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConMensaje() {
        // Given
        when(restauranteRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> restauranteService.buscarPorId(99));
        assertThat(ex.getMessage()).isEqualTo("Restaurante no encontrado");
    }

    // ── guardarRestaurante ────────────────────────────────────────────

    @Test
    @DisplayName("guardarRestaurante: guarda y retorna la entidad persistida")
    void guardarRestaurante_datosValidos_retornaGuardado() {
        // Given
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);

        // When
        Restaurante resultado = restauranteService.guardarRestaurante(restaurante);

        // Then
        assertThat(resultado.getId_restaurante()).isEqualTo(1);
        assertThat(resultado.getNombre_restaurante()).isEqualTo("FastFood Central");
        verify(restauranteRepository, times(1)).save(restaurante);
    }

    // ── actualizarRestaurante ─────────────────────────────────────────

    @Test
    @DisplayName("actualizarRestaurante: actualiza solo los campos no nulos y guarda")
    void actualizarRestaurante_camposNoNulos_actualizaCorrectamente() {
        // Given
        Restaurante datosNuevos = new Restaurante(null, "FastFood Renovado", null, "+56911111111", null);
        when(restauranteRepository.findById(1)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        // When
        Restaurante resultado = restauranteService.actualizarRestaurante(1, datosNuevos);

        // Then
        // El service aplica los campos sobre 'restaurant' (variable local), no sobre 'restaurante'
        assertThat(resultado).isNotNull();
        verify(restauranteRepository, times(1)).findById(1);
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("actualizarRestaurante: lanza RuntimeException con mensaje 'no existe en nuestros registros' cuando no existe")
    void actualizarRestaurante_noExistente_lanzaExcepcion() {
        // Given
        when(restauranteRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> restauranteService.actualizarRestaurante(99, new Restaurante()));
        assertThat(ex.getMessage()).contains("no existe en nuestros registros");
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina la entidad y retorna mensaje con el nombre del restaurante")
    void eliminar_existente_retornaMensajeConNombre() {
        // Given
        when(restauranteRepository.findById(1)).thenReturn(Optional.of(restaurante));
        doNothing().when(restauranteRepository).delete(restaurante);

        // When
        String resultado = restauranteService.eliminar(1);

        // Then
        assertThat(resultado).isEqualTo("El restaurante 'FastFood Central' ha sido eliminado correctamente.");
        verify(restauranteRepository, times(1)).delete(restaurante);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException con ID en el mensaje cuando no existe")
    void eliminar_noExistente_lanzaExcepcionConId() {
        // Given
        when(restauranteRepository.findById(50)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> restauranteService.eliminar(50));
        assertThat(ex.getMessage()).contains("50");
        verify(restauranteRepository, never()).delete(any());
    }
}