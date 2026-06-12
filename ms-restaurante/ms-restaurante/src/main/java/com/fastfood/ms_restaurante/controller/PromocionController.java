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

import com.fastfood.ms_restaurante.DTO.PromocionDTO;
import com.fastfood.ms_restaurante.model.Promocion;
import com.fastfood.ms_restaurante.service.PromocionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @GetMapping
    public ResponseEntity<List<PromocionDTO>> obtenerTodas() {
        List<PromocionDTO> promociones = promocionService.obtenerTodos();
        if (promociones.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(promociones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(promocionService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PromocionDTO> crear(@Valid @RequestBody Promocion promocion) {
        Promocion guardada = promocionService.guardar(promocion);
        return new ResponseEntity<>(promocionService.buscarPorId(guardada.getIdPromocion()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> actualizar(@PathVariable Integer id, @RequestBody Promocion promocion) {
        Promocion actualizada = promocionService.actualizar(id, promocion);
        return new ResponseEntity<>(promocionService.buscarPorId(actualizada.getIdPromocion()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return new ResponseEntity<>(promocionService.eliminar(id), HttpStatus.OK);
    }
}