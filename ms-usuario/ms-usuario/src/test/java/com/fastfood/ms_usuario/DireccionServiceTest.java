package com.fastfood.ms_usuario;

import com.fastfood.ms_usuario.DTO.DireccionDTO;
import com.fastfood.ms_usuario.model.Direccion;
import com.fastfood.ms_usuario.model.Usuario;
import com.fastfood.ms_usuario.repository.DireccionRepository;
import com.fastfood.ms_usuario.service.DireccionService;

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
@DisplayName("Pruebas unitarias - DireccionService")
class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @InjectMocks
    private DireccionService direccionService;

    private Direccion direccion;

    @BeforeEach
    void setUp() {
        // orden de los campos: (idDireccion, calle, numero, depto, referencia, idComuna, idUsuario)
        direccion = new Direccion(1L, "Av. Vicuña Mackenna", "4860", "Depto 502",
                          "Cerca metro Macul", 308, 1L);
    }

    @Test
    @DisplayName("obtenerTodas: retorna lista de DTOs cuando existen direcciones registradas")
    void obtenerTodas_conDatos_retornaListaDTOs() {
        // Given
        Direccion d2 = new Direccion(2L, "Av. Apoquindo", "5400", "Of. 301",
                                     "Cerca metro El Golf", 333, 2L);
        when(direccionRepository.findAll()).thenReturn(Arrays.asList(direccion, d2));

        // When
        List<DireccionDTO> resultado = direccionService.obtenerTodas();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCalle()).isEqualTo("Av. Vicuña Mackenna");
        assertThat(resultado.get(1).getCalle()).isEqualTo("Av. Apoquindo");
        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas: retorna lista vacía cuando no hay direcciones")
    void obtenerTodas_sinDatos_retornaListaVacia() {
        // Given
        when(direccionRepository.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<DireccionDTO> resultado  = direccionService .obtenerTodas();

        // Then
        assertThat(resultado).isEmpty();

    }

    @Test
    @DisplayName("obtenerPorUsuario: retorna lista de DTOs con las direcciones del usuario")
    void obtenerPorUsuario_conDirecciones_retornaListaDTOs() {
        // Given
        Direccion D2 = new Direccion( 3L, "Av. Apoquindo", "5400", "Of. 301", "Cerca metro El Golf", 333, 1L);
        
        when(direccionRepository.findByIdUsuario(1L)).thenReturn(Arrays.asList(direccion, D2));
        
        // When
        List<DireccionDTO> resultado = direccionService.obtenerPorUsuario(1L);
        
        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCalle()).isEqualTo("Av. Vicuña Mackenna");
        assertThat(resultado.get(1).getCalle()).isEqualTo("Av. Apoquindo");
        verify(direccionRepository, times(1)).findByIdUsuario(1L);

    }

    @Test
    @DisplayName("obtenerPorUsuario: retorna lista vacía cuando el usuario no tiene direcciones")
    void obtenerPorUsuario_sinDirecciones_retornaListaVacia() {
        // Given
        when(direccionRepository.findByIdUsuario(99L)).thenReturn(Collections.emptyList());

        // When
        List<DireccionDTO> resultado = direccionService.obtenerPorUsuario(99L);

        // Then
        assertThat(resultado).isEmpty();

    }

}