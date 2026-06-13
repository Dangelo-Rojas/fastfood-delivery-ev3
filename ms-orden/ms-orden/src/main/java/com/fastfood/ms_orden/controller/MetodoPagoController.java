package com.fastfood.ms_orden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_orden.DTO.MetodoPagoDTO;
import com.fastfood.ms_orden.model.MetodoPago;
import com.fastfood.ms_orden.service.MetodoPagoService;

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
@RequestMapping("/api/v1/metodos-pago")
@Tag(name = "Métodos de pago", description = "Gestión de métodos de pago disponibles en el sistema")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Operation(summary = "Obtener todos los métodos de pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = MetodoPagoDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay métodos de pago registrados")
    })
    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> obtenerTodos() {
        List<MetodoPagoDTO> metodos = metodoPagoService.obtenerTodos();
        if (metodos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(metodos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar método de pago por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Método de pago encontrado",
            content = @Content(schema = @Schema(implementation = MetodoPagoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> buscarPorId(
            @Parameter(description = "ID del método de pago", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(metodoPagoService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear método de pago",
               description = "Registra un nuevo método de pago disponible (ej: Tarjeta de crédito, Débito, Efectivo)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Método de pago creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = "{\"nombre\": \"Tarjeta de crédito\", \"descripcion\": \"Visa, Mastercard\"}")))
    @PostMapping
    public ResponseEntity<MetodoPagoDTO> guardar(@Valid @RequestBody MetodoPago metodoPago) {
        return new ResponseEntity<>(metodoPagoService.guardar(metodoPago), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar método de pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Método de pago actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> actualizar(
            @Parameter(description = "ID del método de pago", example = "1") @PathVariable Long id,
            @RequestBody MetodoPago metodoPago) {
        return new ResponseEntity<>(metodoPagoService.actualizar(id, metodoPago), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar método de pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Método de pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del método de pago", example = "1") @PathVariable Long id) {
        return new ResponseEntity<>(metodoPagoService.eliminar(id), HttpStatus.OK);
    }
}
