package com.fastfood.ms_delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_delivery.DTO.DeliveryDTO;
import com.fastfood.ms_delivery.model.Delivery;
import com.fastfood.ms_delivery.service.DeliveryService;

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
@RequestMapping("/api/v1/deliveries")
@Tag(name = "Deliveries", description = "Gestión de entregas a domicilio. Estados válidos: ASIGNADO, EN_CAMINO, ENTREGADO, CANCELADO")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Operation(summary = "Obtener todos los deliveries",
               description = "Retorna la lista completa de entregas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = DeliveryDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay deliveries registrados")
    })
    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> obtenerTodos() {
        List<DeliveryDTO> deliveries = deliveryService.obtenerTodos();
        if (deliveries.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @Operation(summary = "Buscar delivery por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delivery encontrado",
            content = @Content(schema = @Schema(implementation = DeliveryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Delivery no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> buscarPorId(
            @Parameter(description = "ID del delivery", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(deliveryService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener deliveries por estado",
               description = "Retorna todas las entregas en un estado específico. Estados: ASIGNADO, EN_CAMINO, ENTREGADO, CANCELADO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deliveries encontrados",
            content = @Content(schema = @Schema(implementation = DeliveryDTO.class)))
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DeliveryDTO>> obtenerPorEstado(
            @Parameter(description = "Estado del delivery", example = "EN_CAMINO") @PathVariable String estado) {
        return new ResponseEntity<>(deliveryService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @Operation(summary = "Obtener deliveries de un conductor",
               description = "Retorna todas las entregas asignadas a un conductor específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deliveries del conductor obtenidos",
            content = @Content(schema = @Schema(implementation = DeliveryDTO.class)))
    })
    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<List<DeliveryDTO>> obtenerPorConductor(
            @Parameter(description = "ID del conductor", example = "1") @PathVariable Long idConductor) {
        return new ResponseEntity<>(deliveryService.obtenerPorConductor(idConductor), HttpStatus.OK);
    }

    @Operation(summary = "Buscar delivery por orden",
               description = "Retorna el delivery asociado a una orden específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delivery encontrado",
            content = @Content(schema = @Schema(implementation = DeliveryDTO.class))),
        @ApiResponse(responseCode = "404", description = "No existe delivery para esa orden")
    })
    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<DeliveryDTO> buscarPorOrden(
            @Parameter(description = "ID de la orden", example = "1") @PathVariable Long idOrden) {
        return new ResponseEntity<>(deliveryService.buscarPorOrden(idOrden), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo delivery",
               description = "Registra una nueva entrega asignando un conductor a una orden")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Delivery creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"idOrden\": 1, \"idConductor\": 2, \"direccionEntrega\": \"Av. Providencia 123\"}")))
    @PostMapping
    public ResponseEntity<DeliveryDTO> guardar(@Valid @RequestBody Delivery delivery) {
        return new ResponseEntity<>(deliveryService.guardar(delivery), HttpStatus.CREATED);
    }

    @Operation(summary = "Cambiar estado del delivery",
               description = "Actualiza el estado de una entrega. Estados válidos: ASIGNADO, EN_CAMINO, ENTREGADO, CANCELADO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Delivery no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Object> cambiarEstado(
            @Parameter(description = "ID del delivery", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del delivery", example = "EN_CAMINO") @RequestParam String estado) {
        return new ResponseEntity<>(deliveryService.cambiarEstado(id, estado), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar delivery")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delivery eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Delivery no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del delivery", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(deliveryService.eliminar(id), HttpStatus.OK);
    }
}
