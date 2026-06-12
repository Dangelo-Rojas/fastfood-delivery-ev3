package com.fastfood.ms_orden.controller;

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

import com.fastfood.ms_orden.DTO.CarritoDTO;
import com.fastfood.ms_orden.model.Carrito;
import com.fastfood.ms_orden.service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<CarritoDTO>> obtenerTodos() {
        List<CarritoDTO> carritos = carritoService.obtenerTodos();
        if (carritos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(carritos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(carritoService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CarritoDTO> guardar(@Valid @RequestBody Carrito carrito) {
        return new ResponseEntity<>(carritoService.guardar(carrito), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoDTO> actualizar(@PathVariable Integer id, @RequestBody Carrito carrito) {
        return new ResponseEntity<>(carritoService.actualizar(id, carrito), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return new ResponseEntity<>(carritoService.eliminar(id), HttpStatus.OK);
    }
}