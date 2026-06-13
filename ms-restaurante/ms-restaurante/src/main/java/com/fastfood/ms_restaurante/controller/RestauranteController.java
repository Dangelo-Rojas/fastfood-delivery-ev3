package com.fastfood.ms_restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fastfood.ms_restaurante.DTO.RestauranteDTO;
import com.fastfood.ms_restaurante.model.Restaurante;
import com.fastfood.ms_restaurante.service.RestauranteService;

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
@RequestMapping("/api/v1/restaurantes")
@Tag(name = "Restaurantes", description = "Gestión de restaurantes del sistema FastFood Delivery")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @Operation(summary = "Obtener todos los restaurantes",
               description = "Retorna la lista completa de restaurantes registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = RestauranteDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay restaurantes registrados")
    })
    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> obtenerTodos() {
        List<RestauranteDTO> restaurantes = restauranteService.obtenerTodos();
        if (restaurantes.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(restaurantes, HttpStatus.OK);
    }

    @Operation(summary = "Buscar restaurante por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado",
            content = @Content(schema = @Schema(implementation = RestauranteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarPorId(
            @Parameter(description = "ID del restaurante", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(restauranteService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo restaurante",
               description = "Registra un nuevo restaurante en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value =
            "{\"nombre\": \"Burger Palace\", \"direccion\": \"Av. Libertador 456\", \"telefono\": \"+56222345678\"}")))
    @PostMapping
    public ResponseEntity<RestauranteDTO> crear(@Valid @RequestBody Restaurante restaurante) {
        Restaurante guardado = restauranteService.guardarRestaurante(restaurante);
        return new ResponseEntity<>(restauranteService.buscarPorId(guardado.getId_restaurante()), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> actualizar(
            @Parameter(description = "ID del restaurante", example = "1") @PathVariable Integer id,
            @RequestBody Restaurante restaurante) {
        Restaurante actualizado = restauranteService.actualizarRestaurante(id, restaurante);
        return new ResponseEntity<>(restauranteService.buscarPorId(actualizado.getId_restaurante()), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del restaurante", example = "1") @PathVariable Integer id) {
        return new ResponseEntity<>(restauranteService.eliminar(id), HttpStatus.OK);
    }
}
