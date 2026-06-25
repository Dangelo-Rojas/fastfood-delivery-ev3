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

import com.fastfood.ms_usuario.DTO.RegionDTO;
import com.fastfood.ms_usuario.model.Region;
import com.fastfood.ms_usuario.repository.RegionRepository;
import com.fastfood.ms_usuario.service.RegionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - RegionService")
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;


    private Region region;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor -> new Region(idRegion, nombre)
        region = new Region(1L, "Region Metropolitana");
    }

    // ── obtenerTodas

    @Test
    @DisplayName("obtenerTodas: retorna lista de DTOs cuando existen regiones")
    void obtenerTodas_conDatos_retornaListaDTOs() {
        // Given
        Region r2 = new Region(2L, "Valparaiso");
        when(regionRepository.findAll()).thenReturn(Arrays.asList(region, r2));

        // When
        List<RegionDTO> resultado = regionService.obtenerTodas();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Region Metropolitana");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Valparaiso");
        verify(regionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas: retorna lista vacia cuando no hay regiones")
    void obtenerTodas_sinDatos_retornaListaVacia() {
        // Given
        when(regionRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<RegionDTO> resultado = regionService.obtenerTodas();

        // Then
        assertThat(resultado).isEmpty();
        verify(regionRepository, times(1)).findAll();
    }

    // ── buscarPorId

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando la region existe")
    void buscarPorId_existente_retornaDTO() {
        // Given
        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        // When
        RegionDTO resultado = regionService.buscarPorId(1L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdRegion()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Region Metropolitana");
        verify(regionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException cuando la region no existe")
    void buscarPorId_noExistente_lanzaExcepcion() {
        // Given
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> regionService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Region con ID 99 no encontrada");

        verify(regionRepository, times(1)).findById(99L);
    }

    // ── guardar
    @Test
    @DisplayName("guardar: persiste y retorna la region guardada")
    void guardar_regionNueva_retornaRegionPersistida() {
        // Given
        Region nueva = new Region(null, "Biobio");
        Region guardada = new Region(3L, "Biobio");
        when(regionRepository.save(nueva)).thenReturn(guardada);

        // When
        Region resultado = regionService.guardar(nueva);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdRegion()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("Biobio");
        verify(regionRepository, times(1)).save(nueva);
    }

    // ── actualizar 

    @Test
    @DisplayName("actualizar: modifica el nombre cuando la region existe")
    void actualizar_existente_modificaNombre() {
        // Given
        Region datos = new Region(null, "Region Metropolitana de Santiago");
        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        // When
        Region resultado = regionService.actualizar(1L, datos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Region Metropolitana de Santiago");
        verify(regionRepository, times(1)).findById(1L);
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando la region no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        // Given
        Region datos = new Region(null, "Cualquiera");
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> regionService.actualizar(99L, datos))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Region con ID 99 no encontrada");

        verify(regionRepository, times(1)).findById(99L);
        verify(regionRepository, never()).save(any(Region.class));
    }

    // ── eliminar 

    @Test
    @DisplayName("eliminar: retorna mensaje de confirmacion cuando la region existe")
    void eliminar_existente_retornaMensajeExitoso() {
        // Given
        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        doNothing().when(regionRepository).delete(region);

        // When
        String resultado = regionService.eliminar(1L);

        // Then
        assertThat(resultado).isEqualTo("Region Region Metropolitana eliminada exitosamente.");
        verify(regionRepository, times(1)).findById(1L);
        verify(regionRepository, times(1)).delete(region);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException cuando la region no existe")
    void eliminar_noExistente_lanzaExcepcion() {
        // Given
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> regionService.eliminar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Region con ID 99 no encontrada");

        verify(regionRepository, times(1)).findById(99L);
        verify(regionRepository, never()).delete(any(Region.class));
    }
}