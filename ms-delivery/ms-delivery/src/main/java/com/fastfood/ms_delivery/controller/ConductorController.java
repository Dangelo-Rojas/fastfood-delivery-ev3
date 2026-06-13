package com.fastfood.ms_delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_delivery.DTO.ConductorDTO;
import com.fastfood.ms_delivery.model.Conductor;
import com.fastfood.ms_delivery.service.ConductorService;

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
@RequestMapping("/api/v1/conductores")
@Tag(name = "Conductores", description = "Gestión de conductores de delivery")
public class ConductorController {

    @Autowired
    private ConductorService conductorService;

    @Operation(summary = "Obtener todos los conductores")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ConductorDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay conductores registrados")
    })
    @GetMapping
    public ResponseEntity<List<ConductorDTO>> obtenerTodos() {
        List<ConductorDTO> conductores = conductorService.obtenerTodos();
        if (conductores.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(conductores, HttpStatus.OK);
    }

    @Operation(summary = "Obtener conductores disponibles",
               description = "Retorna solo los conductores que actualmente están disponibles para tomar entregas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conductores disponibles obtenidos",
            content = @Content(schema = @Schema(implementation = ConductorDTO.class)))
    })
    @GetMapping("/disponibles")
    public ResponseEntity<List<ConductorDTO>> obtenerDisponibles() {
        return new ResponseEntity<>(conductorService.obtenerDisponibles(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar conductor por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conductor encontrado",
            content = @Content(schema = @Schema(implementation = ConductorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConductorDTO> buscarPorId(
            @Parameter(description = "ID del conductor", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(conductorService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Registrar conductor",
               description = "Registra un nuevo conductor de delivery en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Conductor registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"nombre\": \"Carlos\", \"apellido\": \"Rojas\", \"telefono\": \"+56987654321\", \"patente\": \"ABCD12\"}")))
    @PostMapping
    public ResponseEntity<ConductorDTO> crear(@Valid @RequestBody Conductor conductor) {
        return new ResponseEntity<>(conductorService.guardar(conductor), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar conductor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conductor actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConductorDTO> actualizar(
            @Parameter(description = "ID del conductor", example = "1") @PathVariable Long id,
            @RequestBody Conductor conductor) {
        return new ResponseEntity<>(conductorService.actualizar(id, conductor), HttpStatus.OK);
    }

    @Operation(summary = "Cambiar disponibilidad del conductor",
               description = "Activa o desactiva la disponibilidad de un conductor para recibir entregas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = ConductorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<ConductorDTO> cambiarDisponibilidad(
            @Parameter(description = "ID del conductor", example = "1") @PathVariable Long id,
            @Parameter(description = "Estado de disponibilidad", example = "true") @RequestParam Boolean disponible) {
        return new ResponseEntity<>(conductorService.cambiarDisponibilidad(id, disponible), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar conductor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conductor eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del conductor", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(conductorService.eliminar(id), HttpStatus.OK);
    }
}
