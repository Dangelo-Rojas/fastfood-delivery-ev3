package com.fastfood.ms_usuario;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fastfood.ms_usuario.DTO.ComunaDTO;
import com.fastfood.ms_usuario.model.Comuna;
import com.fastfood.ms_usuario.repository.ComunaRepository;
import com.fastfood.ms_usuario.service.ComunaService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - ComunaService")
class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private ComunaService comunaService;

    private Comuna comuna;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor -> new Comuna(idComuna, nombre, idRegion)
        comuna = new Comuna(1L, "Santiago", 13L);
    }

    // ── obtenerTodas 

    @Test
    @DisplayName("obtenerTodas: retorna lista de DTOs cuando existen comunas")
    void obtenerTodas_conDatos_retornaListaDTOs() {
        // Given
        Comuna c2 = new Comuna(2L, "Providencia", 13L);
        when(comunaRepository.findAll()).thenReturn(Arrays.asList(comuna, c2));

        // When
        List<ComunaDTO> resultado = comunaService.obtenerTodas();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Santiago");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Providencia");
        assertThat(resultado.get(0).getIdRegion()).isEqualTo(13L);
        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas: retorna lista vacia cuando no hay comunas")
    void obtenerTodas_sinDatos_retornaListaVacia() {
        // Given
        when(comunaRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ComunaDTO> resultado = comunaService.obtenerTodas();

        // Then
        assertThat(resultado).isEmpty();
        verify(comunaRepository, times(1)).findAll();
    }

    // ── buscarPorId 

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando la comuna existe")
    void buscarPorId_existente_retornaDTO() {
        // Given
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));

        // When
        ComunaDTO resultado = comunaService.buscarPorId(1L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdComuna()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Santiago");
        assertThat(resultado.getIdRegion()).isEqualTo(13L);
        verify(comunaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException cuando la comuna no existe")
    void buscarPorId_noExistente_lanzaExcepcion() {
        // Given
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> comunaService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Comuna con ID 99 no encontrada");

        verify(comunaRepository, times(1)).findById(99L);
    }

    // ── guardar 

    @Test
    @DisplayName("guardar: persiste y retorna la comuna guardada")
    void guardar_comunaNueva_retornaComunaPersistida() {
        // Given
        Comuna nueva = new Comuna(null, "Las Condes", 13L);
        Comuna guardada = new Comuna(3L, "Las Condes", 13L);
        when(comunaRepository.save(nueva)).thenReturn(guardada);

        // When
        Comuna resultado = comunaService.guardar(nueva);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdComuna()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("Las Condes");
        verify(comunaRepository, times(1)).save(nueva);
    }

    // ── actualizar 

    @Test
    @DisplayName("actualizar: modifica nombre e idRegion cuando la comuna existe")
    void actualizar_existente_modificaCampos() {
        // Given
        Comuna datos = new Comuna(null, "Santiago Centro", 13L);
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comuna);

        // When
        Comuna resultado = comunaService.actualizar(1L, datos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Santiago Centro");
        assertThat(resultado.getIdRegion()).isEqualTo(13L);
        verify(comunaRepository, times(1)).findById(1L);
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando la comuna no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        // Given
        Comuna datos = new Comuna(null, "Cualquiera", 1L);
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> comunaService.actualizar(99L, datos))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Comuna con ID 99 no encontrada");

        verify(comunaRepository, times(1)).findById(99L);
        verify(comunaRepository, never()).save(any(Comuna.class));
    }

    // ── eliminar 

    @Test
    @DisplayName("eliminar: retorna mensaje de confirmacion cuando la comuna existe")
    void eliminar_existente_retornaMensajeExitoso() {
        // Given
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));
        doNothing().when(comunaRepository).delete(comuna);

        // When
        String resultado = comunaService.eliminar(1L);

        // Then
        assertThat(resultado).isEqualTo("Comuna Santiago eliminada exitosamente.");
        verify(comunaRepository, times(1)).findById(1L);
        verify(comunaRepository, times(1)).delete(comuna);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException cuando la comuna no existe")
    void eliminar_noExistente_lanzaExcepcion() {
        // Given
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> comunaService.eliminar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Comuna con ID 99 no encontrada");

        verify(comunaRepository, times(1)).findById(99L);
        verify(comunaRepository, never()).delete(any(Comuna.class));
    }
}