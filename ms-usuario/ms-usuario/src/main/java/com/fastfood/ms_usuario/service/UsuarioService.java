package com.fastfood.ms_usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfood.ms_usuario.DTO.UsuarioDTO;
import com.fastfood.ms_usuario.model.Usuario;
import com.fastfood.ms_usuario.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con ID: " + id);
                });
        return convertirADTO(usuario);
    }

    public Usuario guardar(Usuario usuario) {
        log.info("Guardando nuevo usuario: {}", usuario.getNombre());
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado con ID: {}", guardado.getIdUsuario());
        return guardado;
    }

    public Usuario actualizar(Long id, Usuario datos) {
        log.info("Actualizando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con ID: " + id);
                });
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setEmail(datos.getEmail());
        usuario.setTelefono(datos.getTelefono());
        log.info("Usuario con ID: {} actualizado exitosamente", id);
        return usuarioRepository.save(usuario);
    }

    public String eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con ID: " + id);
                });
        usuarioRepository.delete(usuario);
        log.info("Usuario {} eliminado exitosamente", usuario.getNombre());
        return "Usuario " + usuario.getNombre() + " eliminado exitosamente.";
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        return dto;
    }
}