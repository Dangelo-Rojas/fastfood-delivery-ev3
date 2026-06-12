package com.fastfood.ms_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_usuario.DTO.RegionDTO;
import com.fastfood.ms_usuario.model.Region;
import com.fastfood.ms_usuario.service.RegionService;

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
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "Gestión de regiones geográficas")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Operation(summary = "Obtener todas las regiones",
               description = "Retorna la lista completa de regiones disponibles")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = RegionDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay regiones registradas")
    })
    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodas() {
        List<RegionDTO> regiones = regionService.obtenerTodas();
        if (regiones.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(regiones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar región por ID",
               description = "Retorna los datos de una región específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Región encontrada",
            content = @Content(schema = @Schema(implementation = RegionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> buscarPorId(
            @Parameter(description = "ID de la región", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(regionService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva región",
               description = "Registra una nueva región geográfica")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Región creada exitosamente",
            content = @Content(schema = @Schema(implementation = RegionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = "{\"nombre\": \"Región Metropolitana\"}")))
    @PostMapping
    public ResponseEntity<RegionDTO> crear(@Valid @RequestBody Region region) {
        Region guardada = regionService.guardar(region);
        return new ResponseEntity<>(regionService.buscarPorId(guardada.getIdRegion()), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar región")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Región actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> actualizar(
            @Parameter(description = "ID de la región", example = "1") @PathVariable Long id,
            @RequestBody Region region) {
        Region actualizada = regionService.actualizar(id, region);
        return new ResponseEntity<>(regionService.buscarPorId(actualizada.getIdRegion()), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar región")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Región eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la región", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(regionService.eliminar(id), HttpStatus.OK);
    }
}
