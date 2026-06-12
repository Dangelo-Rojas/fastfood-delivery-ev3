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

import com.fastfood.ms_orden.DTO.MetodoPagoDTO;
import com.fastfood.ms_orden.model.MetodoPago;
import com.fastfood.ms_orden.service.MetodoPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/metodos-pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> obtenerTodos() {
        List<MetodoPagoDTO> metodos = metodoPagoService.obtenerTodos();
        if (metodos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(metodos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(metodoPagoService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MetodoPagoDTO> guardar(@Valid @RequestBody MetodoPago metodoPago) {
        return new ResponseEntity<>(metodoPagoService.guardar(metodoPago), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> actualizar(@PathVariable Long id, @RequestBody MetodoPago metodoPago) {
        return new ResponseEntity<>(metodoPagoService.actualizar(id, metodoPago), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(metodoPagoService.eliminar(id), HttpStatus.OK);
    }
}