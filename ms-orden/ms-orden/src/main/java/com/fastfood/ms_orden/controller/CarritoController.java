package com.fastfood.ms_orden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_orden.DTO.CarritoDTO;
import com.fastfood.ms_orden.model.Carrito;
import com.fastfood.ms_orden.service.CarritoService;

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
@RequestMapping("/api/v1/carritos")
@Tag(name = "Carritos", description = "Gestión de carritos de compra")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Obtener todos los carritos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = CarritoDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay carritos registrados")
    })
    @GetMapping
    public ResponseEntity<List<CarritoDTO>> obtenerTodos() {
        List<CarritoDTO> carritos = carritoService.obtenerTodos();
        if (carritos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(carritos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar carrito por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrito encontrado",
            content = @Content(schema = @Schema(implementation = CarritoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> buscarPorId(
            @Parameter(description = "ID del carrito", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(carritoService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo carrito",
               description = "Crea un nuevo carrito de compra para un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Carrito creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = "{\"idUsuario\": 1}")))
    @PostMapping
    public ResponseEntity<CarritoDTO> guardar(@Valid @RequestBody Carrito carrito) {
        return new ResponseEntity<>(carritoService.guardar(carrito), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrito actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarritoDTO> actualizar(
            @Parameter(description = "ID del carrito", example = "1") @PathVariable Integer id,
            @RequestBody Carrito carrito) {
        return new ResponseEntity<>(carritoService.actualizar(id, carrito), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrito eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del carrito", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(carritoService.eliminar(id), HttpStatus.OK);
    }
}
