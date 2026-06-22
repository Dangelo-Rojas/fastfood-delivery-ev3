package com.fastfood.ms_usuario.service;

import com.fastfood.ms_usuario.DTO.UsuarioDTO;
import com.fastfood.ms_usuario.model.Usuario;
import com.fastfood.ms_usuario.repository.UsuarioRepository;

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
@DisplayName("Pruebas unitarias - UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Lombok @AllArgsConstructor → new Usuario(idUsuario, nombre, apellido, email, telefono)
        usuario = new Usuario(1L, "Dangelo", "Rojas", "dangelo@duoc.cl", "+56912345678");
    }

    // ── obtenerTodos ──────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos: retorna lista de DTOs cuando existen usuarios registrados")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        // Given
        Usuario u2 = new Usuario(2L, "María", "Gómez", "maria@duoc.cl", "+56987654321");
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario, u2));

        // When
        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Dangelo");
        assertThat(resultado.get(1).getNombre()).isEqualTo("María");
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacía cuando no hay usuarios")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO con todos los campos cuando el usuario existe")
    void buscarPorId_existente_retornaDTOCompleto() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        UsuarioDTO resultado = usuarioService.buscarPorId(1L);

        // Then
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Dangelo");
        assertThat(resultado.getApellido()).isEqualTo("Rojas");
        assertThat(resultado.getEmail()).isEqualTo("dangelo@duoc.cl");
        assertThat(resultado.getTelefono()).isEqualTo("+56912345678");
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException con mensaje 'Usuario no encontrado con ID: X' cuando no existe")
    void buscarPorId_noExistente_lanzaExcepcionConMensaje() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarPorId(99L));
        assertThat(ex.getMessage()).isEqualTo("Usuario no encontrado con ID: 99");
    }

    // ── guardar ───────────────────────────────────────────────────────

    @Test
    @DisplayName("guardar: persiste el usuario y retorna la entidad (no DTO)")
    void guardar_datosValidos_retornaUsuarioPersistido() {
        // Given  (guardar() retorna Usuario, no UsuarioDTO)
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.guardar(usuario);

        // Then
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Dangelo");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    // ── actualizar ────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizar: sobreescribe todos los campos del usuario encontrado")
    void actualizar_datosCompletos_actualizaTodosLosCampos() {
        // Given  (el service hace set directo de todos los campos, sin null-check)
        Usuario datos = new Usuario(null, "Carlos", "Soto", "carlos@duoc.cl", "+56900000000");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.actualizar(1L, datos);

        // Then
        assertThat(usuario.getNombre()).isEqualTo("Carlos");
        assertThat(usuario.getApellido()).isEqualTo("Soto");
        assertThat(usuario.getEmail()).isEqualTo("carlos@duoc.cl");
        assertThat(usuario.getTelefono()).isEqualTo("+56900000000");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando el usuario no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizar(99L, new Usuario()));
        assertThat(ex.getMessage()).contains("99");
        verify(usuarioRepository, never()).save(any());
    }

    // ── eliminar ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: elimina y retorna mensaje con el nombre del usuario")
    void eliminar_existente_retornaMensajeConNombre() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        // When
        String resultado = usuarioService.eliminar(1L);

        // Then
        assertThat(resultado).isEqualTo("Usuario Dangelo eliminado exitosamente.");
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException sin llamar a delete cuando no existe")
    void eliminar_noExistente_lanzaExcepcionSinEliminar() {
        // Given
        when(usuarioRepository.findById(50L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> usuarioService.eliminar(50L));
        verify(usuarioRepository, never()).delete(any());
    }
}