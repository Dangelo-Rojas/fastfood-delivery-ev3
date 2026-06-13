package com.fastfood.ms_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_usuario.DTO.DireccionDTO;
import com.fastfood.ms_usuario.model.Direccion;
import com.fastfood.ms_usuario.service.DireccionService;

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
@RequestMapping("/api/v1/direcciones")
@Tag(name = "Direcciones", description = "Gestión de direcciones de entrega de los usuarios")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @Operation(summary = "Obtener todas las direcciones",
               description = "Retorna la lista completa de direcciones registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = DireccionDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay direcciones registradas")
    })
    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerTodas() {
        List<DireccionDTO> direcciones = direccionService.obtenerTodas();
        if (direcciones.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(direcciones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar dirección por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección encontrada",
            content = @Content(schema = @Schema(implementation = DireccionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> buscarPorId(
            @Parameter(description = "ID de la dirección", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(direccionService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener direcciones de un usuario",
               description = "Retorna todas las direcciones asociadas a un usuario específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Direcciones del usuario obtenidas",
            content = @Content(schema = @Schema(implementation = DireccionDTO.class)))
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DireccionDTO>> obtenerPorUsuario(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long idUsuario) {
        return new ResponseEntity<>(direccionService.obtenerPorUsuario(idUsuario), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva dirección",
               description = "Registra una dirección de entrega para un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dirección creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"calle\": \"Av. Providencia\", \"numero\": \"123\", \"idUsuario\": 1, \"idComuna\": 5}")))
    @PostMapping
    public ResponseEntity<DireccionDTO> guardar(@Valid @RequestBody Direccion direccion) {
        return new ResponseEntity<>(direccionService.guardar(direccion), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar dirección")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizar(
            @Parameter(description = "ID de la dirección", example = "1") @PathVariable Long id,
            @RequestBody Direccion direccion) {
        return new ResponseEntity<>(direccionService.actualizar(id, direccion), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar dirección")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la dirección", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(direccionService.eliminar(id), HttpStatus.OK);
    }
}
