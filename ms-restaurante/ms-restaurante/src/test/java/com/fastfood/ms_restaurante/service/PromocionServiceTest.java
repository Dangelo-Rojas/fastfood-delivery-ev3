package com.fastfood.ms_restaurante.service;

import com.fastfood.ms_restaurante.DTO.PromocionDTO;
import com.fastfood.ms_restaurante.model.Promocion;
import com.fastfood.ms_restaurante.repository.PromocionRepository;


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
@DisplayName("Pruebas unitarias - PromocionService")
class PromocionServiceTest {

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionService promocionService;

    private Promocion promocion;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor → new Promocion(id, nombre, descripcion, precio, idCatalogo)
        promocion = new Promocion(1, "2x1 Hamburguesas", "Paga una y lleva dos hamburguesas", 5990.0, 1);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos: retorna lista de DTOs de todas las promociones")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        // Given
        Promocion p2 = new Promocion(2, "Combo Familiar", "Hamburguesa + papas + bebida", 9990.0, 1);
        when(promocionRepository.findAll()).thenReturn(Arrays.asList(promocion, p2));

        // When
        List<PromocionDTO> resultado = promocionService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombrePromocion()).isEqualTo("2x1 Hamburguesas");
        assertThat(resultado.get(1).getNombrePromocion()).isEqualTo("Combo Familiar");
        verify(promocionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacía si no hay promociones registradas")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        // Given
        when(promocionRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<PromocionDTO> resultado = promocionService.obtenerTodos();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO con todos los campos mapeados cuando la promoción existe")
    void buscarPorId_existente_retornaDTOCompleto() {
        // Given
        when(promocionRepository.findById(1)).thenReturn(Optional.of(promocion));

        // When
        PromocionDTO resultado = promocionService.buscarPorId(1);

        // Then
        assertThat(resultado.getIdPromocion()).isEqualTo(1);
        assertThat(resultado.getNombrePromocion()).isEqualTo("2x1 Hamburguesas");
        assertThat(resultado.getPrecioPromocion()).isEqualTo(5990.0);
        assertThat(resultado.getIdCatalogo()).isEqualTo(1);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con mensaje 'Promocion con ID X no encontrada' cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConMensaje() {
        // Given
        when(promocionRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> promocionService.buscarPorId(99));
        assertThat(ex.getMessage()).isEqualTo("Promocion con ID 99 no encontrada");
    }

    // ── guardar ───────────────────────────────────────────────────────

    @Test
    @DisplayName("guardar: persiste y retorna la entidad Promocion (no DTO)")
    void guardar_datosValidos_retornaEntidadPersistida() {
        // Given  (guardar() retorna Promocion, no PromocionDTO)
        when(promocionRepository.save(promocion)).thenReturn(promocion);

        // When
        Promocion resultado = promocionService.guardar(promocion);

        // Then
        assertThat(resultado.getIdPromocion()).isEqualTo(1);
        assertThat(resultado.getNombrePromocion()).isEqualTo("2x1 Hamburguesas");
        verify(promocionRepository, times(1)).save(promocion);
    }

    // ── actualizar ────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizar: actualiza solo los campos no nulos y retorna entidad actualizada")
    void actualizar_soloNuevoPrecioYNombre_actualizaCorrectamente() {
        // Given
        Promocion datos = new Promocion(null, "2x1 Premium", null, 7990.0, null);
        when(promocionRepository.findById(1)).thenReturn(Optional.of(promocion));
        when(promocionRepository.save(promocion)).thenReturn(promocion);

        // When
        Promocion resultado = promocionService.actualizar(1, datos);

        // Then
        assertThat(promocion.getNombrePromocion()).isEqualTo("2x1 Premium");
        assertThat(promocion.getPrecioPromocion()).isEqualTo(7990.0);
        // descripción no cambió porque era null en datos
        assertThat(promocion.getDescripcionPromocion()).isEqualTo("Paga una y lleva dos hamburguesas");
        verify(promocionRepository, times(1)).save(promocion);
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando la promoción no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        // Given
        when(promocionRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> promocionService.actualizar(99, new Promocion()));
        assertThat(ex.getMessage()).contains("99");
        verify(promocionRepository, never()).save(any());
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina y retorna mensaje con el nombre de la promoción")
    void eliminar_existente_retornaMensajeConNombre() {
        // Given
        when(promocionRepository.findById(1)).thenReturn(Optional.of(promocion));
        doNothing().when(promocionRepository).delete(promocion);

        // When
        String resultado = promocionService.eliminar(1);

        // Then
        assertThat(resultado).isEqualTo("Promocion 2x1 Hamburguesas eliminada exitosamente.");
        verify(promocionRepository, times(1)).delete(promocion);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException sin llamar a delete cuando la promoción no existe")
    void eliminar_noExistente_lanzaExcepcionSinEliminar() {
        // Given
        when(promocionRepository.findById(77)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> promocionService.eliminar(77));
        verify(promocionRepository, never()).delete(any());
    }
}