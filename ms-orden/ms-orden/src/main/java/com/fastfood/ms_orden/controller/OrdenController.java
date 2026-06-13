package com.fastfood.ms_orden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_orden.DTO.OrdenDTO;
import com.fastfood.ms_orden.model.Orden;
import com.fastfood.ms_orden.service.OrdenService;

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
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Órdenes", description = "Gestión de órdenes de pedido. Estados válidos: PENDIENTE, EN_PROCESO, EN_CAMINO, ENTREGADO, CANCELADO")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Operation(summary = "Obtener todas las órdenes",
               description = "Retorna la lista completa de órdenes registradas en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = OrdenDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay órdenes registradas")
    })
    @GetMapping
    public ResponseEntity<List<OrdenDTO>> obtenerTodas() {
        List<OrdenDTO> ordenes = ordenService.obtenerTodas();
        if (ordenes.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @Operation(summary = "Buscar orden por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden encontrada",
            content = @Content(schema = @Schema(implementation = OrdenDTO.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> buscarPorId(
            @Parameter(description = "ID de la orden", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(ordenService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener órdenes por estado",
               description = "Retorna todas las órdenes que se encuentran en un estado específico. Estados válidos: PENDIENTE, EN_PROCESO, EN_CAMINO, ENTREGADO, CANCELADO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Órdenes encontradas",
            content = @Content(schema = @Schema(implementation = OrdenDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron órdenes con ese estado")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenDTO>> obtenerPorEstado(
            @Parameter(description = "Estado de la orden", example = "PENDIENTE") @PathVariable String estado) {
        return new ResponseEntity<>(ordenService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @Operation(summary = "Buscar orden por carrito",
               description = "Retorna la orden asociada a un carrito de compra específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden encontrada",
            content = @Content(schema = @Schema(implementation = OrdenDTO.class))),
        @ApiResponse(responseCode = "404", description = "No existe orden para ese carrito")
    })
    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<OrdenDTO> buscarPorCarrito(
            @Parameter(description = "ID del carrito", example = "1") @PathVariable Integer idCarrito) {
        return new ResponseEntity<>(ordenService.buscarPorCarrito(idCarrito), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva orden",
               description = "Crea una nueva orden a partir de un carrito. El total se calcula automáticamente como subtotal - descuento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Orden creada exitosamente",
            content = @Content(schema = @Schema(implementation = OrdenDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"subtotal\": 15990, \"descuento\": 0.0, \"idCarrito\": 1}")))
    @PostMapping
    public ResponseEntity<OrdenDTO> guardar(@Valid @RequestBody Orden orden) {
        return new ResponseEntity<>(ordenService.guardar(orden), HttpStatus.CREATED);
    }

    @Operation(summary = "Cambiar estado de la orden",
               description = "Actualiza el estado de una orden. Estados válidos: PENDIENTE, EN_PROCESO, EN_CAMINO, ENTREGADO, CANCELADO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = OrdenDTO.class))),
        @ApiResponse(responseCode = "400", description = "Estado inválido",
            content = @Content(examples = @ExampleObject(value = "{\"error\": \"Estado inválido: INVALIDO\"}"))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Object> cambiarEstado(
            @Parameter(description = "ID de la orden", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la orden", example = "EN_PROCESO") @RequestParam String estado) {
        return new ResponseEntity<>(ordenService.cambiarEstado(id, estado), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar orden")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la orden", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(ordenService.eliminar(id), HttpStatus.OK);
    }
}
