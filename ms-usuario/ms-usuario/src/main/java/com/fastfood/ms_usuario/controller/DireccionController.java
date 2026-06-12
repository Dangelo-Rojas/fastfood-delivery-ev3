package com.fastfood.ms_usuario.controller;

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

import com.fastfood.ms_usuario.DTO.DireccionDTO;
import com.fastfood.ms_usuario.model.Direccion;
import com.fastfood.ms_usuario.service.DireccionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerTodas() {
        List<DireccionDTO> direcciones = direccionService.obtenerTodas();
        if (direcciones.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(direcciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(direccionService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DireccionDTO>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return new ResponseEntity<>(direccionService.obtenerPorUsuario(idUsuario), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> guardar(@Valid @RequestBody Direccion direccion) {
        return new ResponseEntity<>(direccionService.guardar(direccion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizar(@PathVariable Long id, @RequestBody Direccion direccion) {
        return new ResponseEntity<>(direccionService.actualizar(id, direccion), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(direccionService.eliminar(id), HttpStatus.OK);
    }
}