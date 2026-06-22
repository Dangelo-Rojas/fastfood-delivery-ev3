package com.fastfood.ms_orden.service;

import com.fastfood.ms_orden.DTO.OrdenDTO;
import com.fastfood.ms_orden.model.Orden;
import com.fastfood.ms_orden.repository.OrdenRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - OrdenService")
class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Orden orden;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor → new Orden(id, fechaOrden, estado, subtotal, descuento, total, idCarrito)
        orden = new Orden(1L, LocalDateTime.now(), "PENDIENTE", 10000.0, 1000.0, 9000.0, 5);
    }

    // ── obtenerTodas ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodas: retorna lista de DTOs cuando existen órdenes")
    void obtenerTodas_conDatos_retornaListaDTOs() {
        // Given
        Orden o2 = new Orden(2L, LocalDateTime.now(), "EN_PROCESO", 5000.0, 0.0, 5000.0, 6);
        when(ordenRepository.findAll()).thenReturn(Arrays.asList(orden, o2));

        // When
        List<OrdenDTO> resultado = ordenService.obtenerTodas();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.get(1).getEstado()).isEqualTo("EN_PROCESO");
        verify(ordenRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas: retorna lista vacía cuando no hay órdenes")
    void obtenerTodas_sinDatos_retornaListaVacia() {
        // Given
        when(ordenRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<OrdenDTO> resultado = ordenService.obtenerTodas();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── obtenerPorEstado ──────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorEstado: retorna solo órdenes con el estado indicado")
    void obtenerPorEstado_estadoValido_retornaFiltrado() {
        // Given
        when(ordenRepository.findByEstado("PENDIENTE")).thenReturn(List.of(orden));

        // When
        List<OrdenDTO> resultado = ordenService.obtenerPorEstado("PENDIENTE");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("PENDIENTE");
        verify(ordenRepository, times(1)).findByEstado("PENDIENTE");
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO con todos los campos mapeados cuando la orden existe")
    void buscarPorId_existente_retornaDTOCompleto() {
        // Given
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        // When
        OrdenDTO resultado = ordenService.buscarPorId(1L);

        // Then
        assertThat(resultado.getIdOrden()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getSubtotal()).isEqualTo(10000.0);
        assertThat(resultado.getDescuento()).isEqualTo(1000.0);
        assertThat(resultado.getTotal()).isEqualTo(9000.0);
        assertThat(resultado.getIdCarrito()).isEqualTo(5);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con ID en el mensaje cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConId() {
        // Given
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordenService.buscarPorId(99L));
        assertThat(ex.getMessage()).contains("99");
    }

    // ── buscarPorCarrito ──────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorCarrito: retorna DTO cuando existe orden para el carrito")
    void buscarPorCarrito_carritoConOrden_retornaDTO() {
        // Given
        when(ordenRepository.findByIdCarrito(5)).thenReturn(orden);

        // When
        OrdenDTO resultado = ordenService.buscarPorCarrito(5);

        // Then
        assertThat(resultado.getIdCarrito()).isEqualTo(5);
    }

    @Test
    @DisplayName("buscarPorCarrito: lanza RuntimeException cuando el carrito no tiene orden asociada")
    void buscarPorCarrito_carritoSinOrden_lanzaExcepcion() {
        // Given
        when(ordenRepository.findByIdCarrito(99)).thenReturn(null);

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordenService.buscarPorCarrito(99));
        assertThat(ex.getMessage()).contains("99");
    }

    // ── guardar ───────────────────────────────────────────────────────

    @Test
    @DisplayName("guardar: calcula total como subtotal - descuento y asigna fecha automáticamente")
    void guardar_ordenNueva_calculaTotalYAsignaFecha() {
        // Given — orden sin fecha ni total calculado
        Orden nueva = new Orden(null, null, "PENDIENTE", 15000.0, 1500.0, null, 7);
        Orden guardada = new Orden(2L, LocalDateTime.now(), "PENDIENTE", 15000.0, 1500.0, 13500.0, 7);
        when(ordenRepository.save(any(Orden.class))).thenReturn(guardada);

        // When
        OrdenDTO resultado = ordenService.guardar(nueva);

        // Then
        assertThat(nueva.getFechaOrden()).isNotNull();           // service asigna LocalDateTime.now()
        assertThat(nueva.getTotal()).isEqualTo(13500.0);        // service calcula subtotal - descuento
        assertThat(resultado.getIdOrden()).isEqualTo(2L);
        assertThat(resultado.getTotal()).isEqualTo(13500.0);
        verify(ordenRepository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("guardar: total es igual a subtotal cuando descuento es 0")
    void guardar_sinDescuento_totalEquivaleASubtotal() {
        // Given
        Orden sinDescuento = new Orden(null, null, "PENDIENTE", 8000.0, 0.0, null, 8);
        Orden guardada = new Orden(3L, LocalDateTime.now(), "PENDIENTE", 8000.0, 0.0, 8000.0, 8);
        when(ordenRepository.save(any(Orden.class))).thenReturn(guardada);

        // When
        ordenService.guardar(sinDescuento);

        // Then
        assertThat(sinDescuento.getTotal()).isEqualTo(8000.0);
    }

    // ── cambiarEstado ─────────────────────────────────────────────────

    @Test
    @DisplayName("cambiarEstado: cambia a EN_PROCESO y retorna DTO actualizado")
    void cambiarEstado_aEnProceso_actualizaEstado() {
        // Given
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(orden)).thenReturn(orden);

        // When
        OrdenDTO resultado = ordenService.cambiarEstado(1L, "EN_PROCESO");

        // Then
        assertThat(orden.getEstado()).isEqualTo("EN_PROCESO");
        verify(ordenRepository, times(1)).save(orden);
    }

    @Test
    @DisplayName("cambiarEstado: lanza RuntimeException para estado no definido en el dominio")
    void cambiarEstado_estadoInvalido_lanzaExcepcion() {
        // Given — el service valida antes de buscar en repo

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordenService.cambiarEstado(1L, "ESTADO_INVALIDO"));
        assertThat(ex.getMessage()).contains("Estado inválido");
        verify(ordenRepository, never()).findById(any());
    }

    @Test
    @DisplayName("cambiarEstado: lanza RuntimeException cuando la orden no existe")
    void cambiarEstado_ordenNoExistente_lanzaExcepcion() {
        // Given
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordenService.cambiarEstado(99L, "CANCELADO"));
        assertThat(ex.getMessage()).contains("99");
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina y retorna mensaje con el ID de la orden")
    void eliminar_existente_retornaMensajeConId() {
        // Given
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        doNothing().when(ordenRepository).delete(orden);

        // When
        String resultado = ordenService.eliminar(1L);

        // Then
        assertThat(resultado).isEqualTo("Orden con ID 1 eliminada exitosamente.");
        verify(ordenRepository, times(1)).delete(orden);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException sin llamar a delete cuando no existe")
    void eliminar_noExistente_lanzaExcepcionSinEliminar() {
        // Given
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> ordenService.eliminar(99L));
        verify(ordenRepository, never()).delete(any());
    }
}