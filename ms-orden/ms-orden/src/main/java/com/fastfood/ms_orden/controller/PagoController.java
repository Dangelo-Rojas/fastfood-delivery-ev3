package com.fastfood.ms_orden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_orden.DTO.PagoDTO;
import com.fastfood.ms_orden.model.Pago;
import com.fastfood.ms_orden.service.PagoService;

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
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Gestión de pagos de órdenes")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Obtener todos los pagos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = PagoDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay pagos registrados")
    })
    @GetMapping
    public ResponseEntity<List<PagoDTO>> obtenerTodos() {
        List<PagoDTO> pagos = pagoService.obtenerTodos();
        if (pagos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar pago por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado",
            content = @Content(schema = @Schema(implementation = PagoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> buscarPorId(
            @Parameter(description = "ID del pago", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(pagoService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener pagos de una orden",
               description = "Retorna todos los pagos asociados a una orden específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos de la orden obtenidos",
            content = @Content(schema = @Schema(implementation = PagoDTO.class)))
    })
    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<List<PagoDTO>> obtenerPorOrden(
            @Parameter(description = "ID de la orden", example = "1") @PathVariable Long idOrden) {
        return new ResponseEntity<>(pagoService.obtenerPorOrden(idOrden), HttpStatus.OK);
    }

    @Operation(summary = "Registrar pago",
               description = "Registra un nuevo pago para una orden")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pago registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"monto\": 15990, \"idOrden\": 1, \"idMetodoPago\": 2}")))
    @PostMapping
    public ResponseEntity<PagoDTO> guardar(@Valid @RequestBody Pago pago) {
        return new ResponseEntity<>(pagoService.guardar(pago), HttpStatus.CREATED);
    }

    @Operation(summary = "Cambiar estado del pago",
               description = "Actualiza el estado de un pago (ej: PENDIENTE, APROBADO, RECHAZADO)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<PagoDTO> cambiarEstado(
            @Parameter(description = "ID del pago", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pago", example = "APROBADO") @RequestParam String estado) {
        return new ResponseEntity<>(pagoService.cambiarEstado(id, estado), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del pago", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(pagoService.eliminar(id), HttpStatus.OK);
    }
}
