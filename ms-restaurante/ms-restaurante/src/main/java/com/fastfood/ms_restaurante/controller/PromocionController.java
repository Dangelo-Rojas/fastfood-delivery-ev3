package com.fastfood.ms_restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_restaurante.DTO.PromocionDTO;
import com.fastfood.ms_restaurante.model.Promocion;
import com.fastfood.ms_restaurante.service.PromocionService;

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
@RequestMapping("/api/v1/promociones")
@Tag(name = "Promociones", description = "Gestión de promociones y descuentos de los restaurantes")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @Operation(summary = "Obtener todas las promociones")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = PromocionDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay promociones activas")
    })
    @GetMapping
    public ResponseEntity<List<PromocionDTO>> obtenerTodas() {
        List<PromocionDTO> promociones = promocionService.obtenerTodos();
        if (promociones.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(promociones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar promoción por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Promoción encontrada",
            content = @Content(schema = @Schema(implementation = PromocionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> buscarPorId(
            @Parameter(description = "ID de la promoción", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(promocionService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva promoción",
               description = "Registra una nueva promoción con porcentaje de descuento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Promoción creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"descripcion\": \"2x1 en hamburguesas\", \"descuento\": 50, \"idRestaurante\": 1}")))
    @PostMapping
    public ResponseEntity<PromocionDTO> crear(@Valid @RequestBody Promocion promocion) {
        Promocion guardada = promocionService.guardar(promocion);
        return new ResponseEntity<>(promocionService.buscarPorId(guardada.getIdPromocion()), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar promoción")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Promoción actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> actualizar(
            @Parameter(description = "ID de la promoción", example = "1") @PathVariable Integer id,
            @RequestBody Promocion promocion) {
        Promocion actualizada = promocionService.actualizar(id, promocion);
        return new ResponseEntity<>(promocionService.buscarPorId(actualizada.getIdPromocion()), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar promoción")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Promoción eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la promoción", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(promocionService.eliminar(id), HttpStatus.OK);
    }
}
