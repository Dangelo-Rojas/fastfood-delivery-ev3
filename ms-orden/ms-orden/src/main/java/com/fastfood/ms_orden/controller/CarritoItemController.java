package com.fastfood.ms_orden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_orden.DTO.CarritoItemDTO;
import com.fastfood.ms_orden.model.CarritoItem;
import com.fastfood.ms_orden.service.CarritoItemService;

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
@RequestMapping("/api/v1/carrito-items")
@Tag(name = "Items del carrito", description = "Gestión de productos dentro de un carrito de compra")
public class CarritoItemController {

    @Autowired
    private CarritoItemService carritoItemService;

    @Operation(summary = "Obtener todos los items")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = CarritoItemDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay items registrados")
    })
    @GetMapping
    public ResponseEntity<List<CarritoItemDTO>> obtenerTodos() {
        List<CarritoItemDTO> items = carritoItemService.obtenerTodos();
        if (items.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Operation(summary = "Buscar item por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item encontrado",
            content = @Content(schema = @Schema(implementation = CarritoItemDTO.class))),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> buscarPorId(
            @Parameter(description = "ID del item", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(carritoItemService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener items de un carrito",
               description = "Retorna todos los productos que pertenecen a un carrito específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Items del carrito obtenidos",
            content = @Content(schema = @Schema(implementation = CarritoItemDTO.class)))
    })
    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<List<CarritoItemDTO>> obtenerPorCarrito(
            @Parameter(description = "ID del carrito", example = "1") @PathVariable Integer idCarrito) {
        return new ResponseEntity<>(carritoItemService.obtenerPorCarrito(idCarrito), HttpStatus.OK);
    }

    @Operation(summary = "Agregar producto al carrito",
               description = "Agrega un producto del catálogo a un carrito de compra con su cantidad")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"idCarrito\": 1, \"idProducto\": 3, \"cantidad\": 2, \"precioUnitario\": 5990}")))
    @PostMapping
    public ResponseEntity<CarritoItemDTO> guardar(@Valid @RequestBody CarritoItem item) {
        return new ResponseEntity<>(carritoItemService.guardar(item), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar item del carrito",
               description = "Modifica la cantidad u otros datos de un producto en el carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> actualizar(
            @Parameter(description = "ID del item", example = "1") @PathVariable Integer id,
            @RequestBody CarritoItem item) {
        return new ResponseEntity<>(carritoItemService.actualizar(id, item), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar item del carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del item", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(carritoItemService.eliminar(id), HttpStatus.OK);
    }
}
