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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfood.ms_orden.DTO.OrdenDTO;
import com.fastfood.ms_orden.model.Orden;
import com.fastfood.ms_orden.service.OrdenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public ResponseEntity<List<OrdenDTO>> obtenerTodas() {
        List<OrdenDTO> ordenes = ordenService.obtenerTodas();
        if (ordenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(ordenService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenDTO>> obtenerPorEstado(@PathVariable String estado) {
        return new ResponseEntity<>(ordenService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<OrdenDTO> buscarPorCarrito(@PathVariable Integer idCarrito) {
        return new ResponseEntity<>(ordenService.buscarPorCarrito(idCarrito), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrdenDTO> guardar(@Valid @RequestBody Orden orden) {
        return new ResponseEntity<>(ordenService.guardar(orden), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Object> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return new ResponseEntity<>(ordenService.cambiarEstado(id, estado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(ordenService.eliminar(id), HttpStatus.OK);
    }
}