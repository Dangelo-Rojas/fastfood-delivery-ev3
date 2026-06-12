package com.fastfood.ms_delivery.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfood.ms_delivery.DTO.ConductorDTO;
import com.fastfood.ms_delivery.model.Conductor;
import com.fastfood.ms_delivery.service.ConductorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/conductores")
public class ConductorController {

    @Autowired
    private ConductorService conductorService;

    @GetMapping
    public ResponseEntity<List<ConductorDTO>> obtenerTodos() {
        List<ConductorDTO> conductores = conductorService.obtenerTodos();
        if (conductores.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(conductores, HttpStatus.OK);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ConductorDTO>> obtenerDisponibles() {
        return new ResponseEntity<>(conductorService.obtenerDisponibles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(conductorService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ConductorDTO> crear(@Valid @RequestBody Conductor conductor) {
        return new ResponseEntity<>(conductorService.guardar(conductor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConductorDTO> actualizar(@PathVariable Long id, @RequestBody Conductor conductor) {
        return new ResponseEntity<>(conductorService.actualizar(id, conductor), HttpStatus.OK);
    }

    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<ConductorDTO> cambiarDisponibilidad(@PathVariable Long id, @RequestParam Boolean disponible) {
        return new ResponseEntity<>(conductorService.cambiarDisponibilidad(id, disponible), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(conductorService.eliminar(id), HttpStatus.OK);
    }
}