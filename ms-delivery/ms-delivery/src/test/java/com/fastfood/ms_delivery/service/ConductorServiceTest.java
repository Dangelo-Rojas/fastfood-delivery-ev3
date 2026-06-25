package com.fastfood.ms_delivery.service;

import com.fastfood.ms_delivery.DTO.ConductorDTO;
import com.fastfood.ms_delivery.model.Conductor;
import com.fastfood.ms_delivery.repository.ConductorRepository;


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
@DisplayName("Pruebas unitarias - ConductorService")
class ConductorServiceTest {

    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private ConductorService conductorService;

    private Conductor conductor;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor → new Conductor(id, nombre, apellido, telefono, patente, disponible)
        conductor = new Conductor(1L, "Juan", "Pérez", "+56912345678", "ABCD12", true);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos: retorna lista de DTOs cuando hay conductores registrados")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        // Given
        Conductor c2 = new Conductor(2L, "María", "López", "+56987654321", "EFGH34", false);
        when(conductorRepository.findAll()).thenReturn(Arrays.asList(conductor, c2));

        // When
        List<ConductorDTO> resultado = conductorService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        assertThat(resultado.get(1).getNombre()).isEqualTo("María");
        verify(conductorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacía cuando no hay conductores")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        // Given
        when(conductorRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ConductorDTO> resultado = conductorService.obtenerTodos();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── obtenerDisponibles ────────────────────────────────────────────

    @Test
    @DisplayName("obtenerDisponibles: retorna solo conductores con disponible=true")
    void obtenerDisponibles_conConductoresDisponibles_retornaFiltrado() {
        // Given
        when(conductorRepository.findByDisponible(true)).thenReturn(List.of(conductor));

        // When
        List<ConductorDTO> resultado = conductorService.obtenerDisponibles();

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDisponible()).isTrue();
        verify(conductorRepository, times(1)).findByDisponible(true);
    }

    @Test
    @DisplayName("obtenerDisponibles: retorna lista vacía si ningún conductor está disponible")
    void obtenerDisponibles_sinDisponibles_retornaVacio() {
        // Given
        when(conductorRepository.findByDisponible(true)).thenReturn(Collections.emptyList());

        // When
        List<ConductorDTO> resultado = conductorService.obtenerDisponibles();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO con todos los campos cuando el conductor existe")
    void buscarPorId_existente_retornaDTOCompleto() {
        // Given
        when(conductorRepository.findById(1L)).thenReturn(Optional.of(conductor));

        // When
        ConductorDTO resultado = conductorService.buscarPorId(1L);

        // Then
        assertThat(resultado.getIdConductor()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Pérez");
        assertThat(resultado.getTelefono()).isEqualTo("+56912345678");
        assertThat(resultado.getPatenteVehiculo()).isEqualTo("ABCD12");
        assertThat(resultado.getDisponible()).isTrue();
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con mensaje 'Conductor con ID X no encontrado' cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConMensaje() {
        // Given
        when(conductorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> conductorService.buscarPorId(99L));
        assertThat(ex.getMessage()).isEqualTo("Conductor con ID 99 no encontrado");
    }

    // ── guardar ───────────────────────────────────────────────────────

    @Test
    @DisplayName("guardar: persiste el conductor y retorna DTO")
    void guardar_datosValidos_retornaDTO() {
        // Given
        when(conductorRepository.save(conductor)).thenReturn(conductor);

        // When
        ConductorDTO resultado = conductorService.guardar(conductor);

        // Then
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Pérez");
        verify(conductorRepository, times(1)).save(conductor);
    }

    // ── actualizar ────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizar: sobreescribe todos los campos del conductor encontrado")
    void actualizar_datosCompletos_actualizaTodosLosCampos() {
        // Given  (el service hace set directo de todos los campos, sin null-check)
        Conductor datos = new Conductor(null, "Carlos", "Soto", "+56900000000", "XY9900", null);
        when(conductorRepository.findById(1L)).thenReturn(Optional.of(conductor));
        when(conductorRepository.save(conductor)).thenReturn(conductor);

        // When
        ConductorDTO resultado = conductorService.actualizar(1L, datos);

        // Then
        assertThat(conductor.getNombre()).isEqualTo("Carlos");
        assertThat(conductor.getApellido()).isEqualTo("Soto");
        assertThat(conductor.getTelefono()).isEqualTo("+56900000000");
        assertThat(conductor.getPatenteVehiculo()).isEqualTo("XY9900");
        verify(conductorRepository, times(1)).save(conductor);
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando el conductor no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        // Given
        when(conductorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> conductorService.actualizar(99L, new Conductor()));
        assertThat(ex.getMessage()).contains("99");
        verify(conductorRepository, never()).save(any());
    }

    // ── cambiarDisponibilidad ─────────────────────────────────────────

    @Test
    @DisplayName("cambiarDisponibilidad: cambia a false y retorna DTO actualizado")
    void cambiarDisponibilidad_aFalse_actualizaYRetornaDTO() {
        // Given
        when(conductorRepository.findById(1L)).thenReturn(Optional.of(conductor));
        when(conductorRepository.save(conductor)).thenReturn(conductor);

        // When
        ConductorDTO resultado = conductorService.cambiarDisponibilidad(1L, false);

        // Then
        assertThat(conductor.getDisponible()).isFalse();
        verify(conductorRepository, times(1)).save(conductor);
    }

    @Test
    @DisplayName("cambiarDisponibilidad: lanza RuntimeException cuando el conductor no existe")
    void cambiarDisponibilidad_noExistente_lanzaExcepcion() {
        // Given
        when(conductorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> conductorService.cambiarDisponibilidad(99L, true));
        verify(conductorRepository, never()).save(any());
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina y retorna mensaje con el nombre del conductor")
    void eliminar_existente_retornaMensajeConNombre() {
        // Given
        when(conductorRepository.findById(1L)).thenReturn(Optional.of(conductor));
        doNothing().when(conductorRepository).delete(conductor);

        // When
        String resultado = conductorService.eliminar(1L);

        // Then
        assertThat(resultado).isEqualTo("Conductor Juan eliminado exitosamente.");
        verify(conductorRepository, times(1)).delete(conductor);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException sin llamar a delete cuando no existe")
    void eliminar_noExistente_lanzaExcepcionSinEliminar() {
        // Given
        when(conductorRepository.findById(50L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> conductorService.eliminar(50L));
        verify(conductorRepository, never()).delete(any());
    }
}