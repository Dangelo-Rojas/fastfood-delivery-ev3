package com.fastfood.ms_restaurante.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfood.ms_restaurante.DTO.RestauranteDTO;
import com.fastfood.ms_restaurante.model.Restaurante;
import com.fastfood.ms_restaurante.service.RestauranteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> obtenerTodos() {
        List<RestauranteDTO> restaurantes = restauranteService.obtenerTodos();
        if (restaurantes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(restaurantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(restauranteService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestauranteDTO> crear(@Valid @RequestBody Restaurante restaurante) {
        Restaurante guardado = restauranteService.guardarRestaurante(restaurante);
        return new ResponseEntity<>(restauranteService.buscarPorId(guardado.getId_restaurante()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> actualizar(@PathVariable Integer id, @RequestBody Restaurante restaurante) {
        Restaurante actualizado = restauranteService.actualizarRestaurante(id, restaurante);
        return new ResponseEntity<>(restauranteService.buscarPorId(actualizado.getId_restaurante()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return new ResponseEntity<>(restauranteService.eliminar(id), HttpStatus.OK);
    }
}
