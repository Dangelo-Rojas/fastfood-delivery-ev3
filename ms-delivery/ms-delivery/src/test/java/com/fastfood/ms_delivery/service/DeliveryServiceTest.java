package com.fastfood.ms_delivery.service;

import com.fastfood.ms_delivery.DTO.DeliveryDTO;
import com.fastfood.ms_delivery.model.Delivery;
import com.fastfood.ms_delivery.repository.DeliveryRepository;


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
@DisplayName("Pruebas unitarias - DeliveryService")
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery delivery;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor → new Delivery(id, horaSalida, horaLlegada, estado, idOrden, idConductor)
        delivery = new Delivery(1L, LocalDateTime.now(), null, "PENDIENTE", 10L, 2L);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos: retorna lista de DTOs cuando existen deliveries")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        // Given
        Delivery d2 = new Delivery(2L, LocalDateTime.now(), null, "EN_CAMINO", 20L, 3L);
        when(deliveryRepository.findAll()).thenReturn(Arrays.asList(delivery, d2));

        // When
        List<DeliveryDTO> resultado = deliveryService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.get(1).getEstado()).isEqualTo("EN_CAMINO");
        verify(deliveryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacía cuando no hay deliveries")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        // Given
        when(deliveryRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<DeliveryDTO> resultado = deliveryService.obtenerTodos();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── obtenerPorEstado ──────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorEstado: retorna solo deliveries con el estado solicitado")
    void obtenerPorEstado_estadoValido_retornaFiltrado() {
        // Given
        when(deliveryRepository.findByEstado("EN_CAMINO")).thenReturn(List.of(
                new Delivery(3L, LocalDateTime.now(), null, "EN_CAMINO", 30L, 2L)
        ));

        // When
        List<DeliveryDTO> resultado = deliveryService.obtenerPorEstado("EN_CAMINO");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("EN_CAMINO");
        verify(deliveryRepository, times(1)).findByEstado("EN_CAMINO");
    }

    // ── obtenerPorConductor ───────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorConductor: retorna los deliveries asignados al conductor")
    void obtenerPorConductor_conductorExistente_retornaLista() {
        // Given
        when(deliveryRepository.findByIdConductor(2L)).thenReturn(List.of(delivery));

        // When
        List<DeliveryDTO> resultado = deliveryService.obtenerPorConductor(2L);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdConductor()).isEqualTo(2L);
        verify(deliveryRepository, times(1)).findByIdConductor(2L);
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO con todos los campos cuando el delivery existe")
    void buscarPorId_existente_retornaDTOCompleto() {
        // Given
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        // When
        DeliveryDTO resultado = deliveryService.buscarPorId(1L);

        // Then
        assertThat(resultado.getIdDelivery()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getIdOrden()).isEqualTo(10L);
        assertThat(resultado.getIdConductor()).isEqualTo(2L);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con ID en el mensaje cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConId() {
        // Given
        when(deliveryRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> deliveryService.buscarPorId(99L));
        assertThat(ex.getMessage()).contains("99");
    }

    // ── buscarPorOrden ────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorOrden: retorna DTO cuando existe delivery para la orden")
    void buscarPorOrden_ordenConDelivery_retornaDTO() {
        // Given
        when(deliveryRepository.findByIdOrden(10L)).thenReturn(delivery);

        // When
        DeliveryDTO resultado = deliveryService.buscarPorOrden(10L);

        // Then
        assertThat(resultado.getIdOrden()).isEqualTo(10L);
    }

    @Test
    @DisplayName("buscarPorOrden: lanza RuntimeException cuando no existe delivery para la orden")
    void buscarPorOrden_ordenSinDelivery_lanzaExcepcion() {
        // Given
        when(deliveryRepository.findByIdOrden(99L)).thenReturn(null);

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> deliveryService.buscarPorOrden(99L));
        assertThat(ex.getMessage()).contains("99");
    }

    // ── guardar ───────────────────────────────────────────────────────

    @Test
    @DisplayName("guardar: asigna horaSalida automáticamente y retorna DTO persistido")
    void guardar_deliveryNuevo_asignaHoraSalidaYRetornaDTO() {
        // Given — delivery sin horaSalida, el service la setea internamente
        Delivery nuevo = new Delivery(null, null, null, "PENDIENTE", 10L, 2L);
        Delivery guardado = new Delivery(1L, LocalDateTime.now(), null, "PENDIENTE", 10L, 2L);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(guardado);

        // When
        DeliveryDTO resultado = deliveryService.guardar(nuevo);

        // Then
        assertThat(nuevo.getHoraSalida()).isNotNull();   // el service asignó LocalDateTime.now()
        assertThat(resultado.getIdDelivery()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        verify(deliveryRepository, times(1)).save(nuevo);
    }

    // ── cambiarEstado ─────────────────────────────────────────────────

    @Test
    @DisplayName("cambiarEstado: cambia a EN_CAMINO sin tocar horaLlegada")
    void cambiarEstado_aEnCamino_actualizaEstadoSinHoraLlegada() {
        // Given
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(delivery)).thenReturn(delivery);

        // When
        DeliveryDTO resultado = deliveryService.cambiarEstado(1L, "EN_CAMINO");

        // Then
        assertThat(delivery.getEstado()).isEqualTo("EN_CAMINO");
        assertThat(delivery.getHoraLlegada()).isNull();
        verify(deliveryRepository, times(1)).save(delivery);
    }

    @Test
    @DisplayName("cambiarEstado: asigna horaLlegada automáticamente al cambiar a ENTREGADO")
    void cambiarEstado_aEntregado_asignaHoraLlegada() {
        // Given
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(delivery)).thenReturn(delivery);

        // When
        deliveryService.cambiarEstado(1L, "ENTREGADO");

        // Then
        assertThat(delivery.getEstado()).isEqualTo("ENTREGADO");
        assertThat(delivery.getHoraLlegada()).isNotNull();
    }

    @Test
    @DisplayName("cambiarEstado: lanza RuntimeException cuando el estado no es válido")
    void cambiarEstado_estadoInvalido_lanzaExcepcion() {
        // Given — no se hace ningún mock de repository porque el service valida primero

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> deliveryService.cambiarEstado(1L, "ESTADO_INVALIDO"));
        assertThat(ex.getMessage()).contains("Estado inválido");
        verify(deliveryRepository, never()).findById(any());
    }

    @Test
    @DisplayName("cambiarEstado: lanza RuntimeException cuando el delivery no existe")
    void cambiarEstado_deliveryNoExistente_lanzaExcepcion() {
        // Given
        when(deliveryRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> deliveryService.cambiarEstado(99L, "CANCELADO"));
        assertThat(ex.getMessage()).contains("99");
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina y retorna mensaje con el ID del delivery")
    void eliminar_existente_retornaMensajeConId() {
        // Given
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        doNothing().when(deliveryRepository).delete(delivery);

        // When
        String resultado = deliveryService.eliminar(1L);

        // Then
        assertThat(resultado).isEqualTo("Delivery con ID 1 eliminado exitosamente.");
        verify(deliveryRepository, times(1)).delete(delivery);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException sin llamar a delete cuando no existe")
    void eliminar_noExistente_lanzaExcepcionSinEliminar() {
        // Given
        when(deliveryRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> deliveryService.eliminar(99L));
        verify(deliveryRepository, never()).delete(any());
    }
}