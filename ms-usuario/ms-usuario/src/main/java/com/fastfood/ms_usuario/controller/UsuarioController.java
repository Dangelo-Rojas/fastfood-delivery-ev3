package com.fastfood.ms_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_usuario.DTO.UsuarioDTO;
import com.fastfood.ms_usuario.model.Usuario;
import com.fastfood.ms_usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema FastFood Delivery")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios",
               description = "Retorna la lista completa de usuarios registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios registrados")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        if (usuarios.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuario por ID",
               description = "Retorna los datos de un usuario específico según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content(examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado con ID: 1\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(
            @Parameter(description = "ID del usuario a buscar", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo usuario",
               description = "Registra un nuevo usuario en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
            content = @Content(examples = @ExampleObject(value = "{\"error\": \"El email no tiene un formato válido\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del nuevo usuario",
        content = @Content(examples = @ExampleObject(value =
            "{\"nombre\": \"Juan\", \"apellido\": \"Pérez\", \"email\": \"juan@correo.com\", \"telefono\": \"+56912345678\"}")))
    @PostMapping
    public ResponseEntity<UsuarioDTO> guardar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.guardar(usuario);
        return new ResponseEntity<>(usuarioService.buscarPorId(nuevo.getIdUsuario()), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar usuario",
               description = "Actualiza los datos de un usuario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(
            @Parameter(description = "ID del usuario a actualizar", example = "1") @PathVariable Long id,
            @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        return new ResponseEntity<>(usuarioService.buscarPorId(actualizado.getIdUsuario()), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar usuario",
               description = "Elimina un usuario del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente",
            content = @Content(examples = @ExampleObject(value = "\"Usuario Juan eliminado exitosamente.\""))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del usuario a eliminar", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.eliminar(id), HttpStatus.OK);
    }
}
