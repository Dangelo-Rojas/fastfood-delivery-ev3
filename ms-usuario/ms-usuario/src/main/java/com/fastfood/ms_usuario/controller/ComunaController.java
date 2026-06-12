package com.fastfood.ms_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_usuario.DTO.ComunaDTO;
import com.fastfood.ms_usuario.model.Comuna;
import com.fastfood.ms_usuario.service.ComunaService;

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
@RequestMapping("/api/v1/comunas")
@Tag(name = "Comunas", description = "Gestión de comunas asociadas a regiones")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @Operation(summary = "Obtener todas las comunas",
               description = "Retorna la lista completa de comunas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de comunas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ComunaDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay comunas registradas")
    })
    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodas() {
        List<ComunaDTO> comunas = comunaService.obtenerTodas();
        if (comunas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar comuna por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comuna encontrada",
            content = @Content(schema = @Schema(implementation = ComunaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> buscarPorId(
            @Parameter(description = "ID de la comuna", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(comunaService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva comuna",
               description = "Registra una nueva comuna en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comuna creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = "{\"nombre\": \"Las Condes\", \"idRegion\": 1}")))
    @PostMapping
    public ResponseEntity<ComunaDTO> crear(@Valid @RequestBody Comuna comuna) {
        Comuna guardada = comunaService.guardar(comuna);
        return new ResponseEntity<>(comunaService.buscarPorId(guardada.getIdComuna()), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar comuna")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comuna actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ComunaDTO> actualizar(
            @Parameter(description = "ID de la comuna", example = "1") @PathVariable Long id,
            @RequestBody Comuna comuna) {
        Comuna actualizada = comunaService.actualizar(id, comuna);
        return new ResponseEntity<>(comunaService.buscarPorId(actualizada.getIdComuna()), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar comuna")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comuna eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la comuna", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(comunaService.eliminar(id), HttpStatus.OK);
    }
}
