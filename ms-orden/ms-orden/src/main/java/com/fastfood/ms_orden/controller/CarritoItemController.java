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

import com.fastfood.ms_orden.DTO.CarritoItemDTO;
import com.fastfood.ms_orden.model.CarritoItem;
import com.fastfood.ms_orden.service.CarritoItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carrito-items")
public class CarritoItemController {

    @Autowired
    private CarritoItemService carritoItemService;

    @GetMapping
    public ResponseEntity<List<CarritoItemDTO>> obtenerTodos() {
        List<CarritoItemDTO> items = carritoItemService.obtenerTodos();
        if (items.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(carritoItemService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<List<CarritoItemDTO>> obtenerPorCarrito(@PathVariable Integer idCarrito) {
        return new ResponseEntity<>(carritoItemService.obtenerPorCarrito(idCarrito), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CarritoItemDTO> guardar(@Valid @RequestBody CarritoItem item) {
        return new ResponseEntity<>(carritoItemService.guardar(item), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> actualizar(@PathVariable Integer id, @RequestBody CarritoItem item) {
        return new ResponseEntity<>(carritoItemService.actualizar(id, item), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return new ResponseEntity<>(carritoItemService.eliminar(id), HttpStatus.OK);
    }
}