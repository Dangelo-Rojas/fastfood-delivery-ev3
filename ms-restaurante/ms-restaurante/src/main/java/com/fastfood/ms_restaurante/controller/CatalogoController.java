package com.fastfood.ms_restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_restaurante.DTO.CatalogoDTO;
import com.fastfood.ms_restaurante.model.Catalogo;
import com.fastfood.ms_restaurante.service.CatalogoService;

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
@RequestMapping("/api/v1/catalogos")
@Tag(name = "Catálogo", description = "Gestión del catálogo de productos de los restaurantes")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(summary = "Obtener todos los productos del catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Catálogo obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = CatalogoDTO.class))),
        @ApiResponse(responseCode = "204", description = "Catálogo vacío")
    })
    @GetMapping
    public ResponseEntity<List<CatalogoDTO>> obtenerTodos() {
        List<CatalogoDTO> catalogos = catalogoService.obtenerTodos();
        if (catalogos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(catalogos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar producto por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = CatalogoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoDTO> buscarPorId(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(catalogoService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Agregar producto al catálogo",
               description = "Registra un nuevo producto disponible en un restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"nombre\": \"Hamburguesa Clásica\", \"descripcion\": \"Carne, lechuga, tomate\", \"precio\": 5990, \"idRestaurante\": 1}")))
    @PostMapping
    public ResponseEntity<CatalogoDTO> crear(@Valid @RequestBody Catalogo catalogo) {
        return new ResponseEntity<>(catalogoService.guardar(catalogo), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar producto del catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CatalogoDTO> actualizar(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Integer id,
            @RequestBody Catalogo catalogo) {
        return new ResponseEntity<>(catalogoService.actualizar(id, catalogo), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar producto del catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(catalogoService.eliminar(id), HttpStatus.OK);
    }
}
