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

import com.fastfood.ms_usuario.DTO.ComunaDTO;
import com.fastfood.ms_usuario.model.Comuna;
import com.fastfood.ms_usuario.service.ComunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodas() {
        List<ComunaDTO> comunas = comunaService.obtenerTodas();
        if (comunas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(comunaService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ComunaDTO> crear(@Valid @RequestBody Comuna comuna) {
        Comuna guardada = comunaService.guardar(comuna);
        return new ResponseEntity<>(comunaService.buscarPorId(guardada.getIdComuna()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaDTO> actualizar(@PathVariable Long id, @RequestBody Comuna comuna) {
        Comuna actualizada = comunaService.actualizar(id, comuna);
        return new ResponseEntity<>(comunaService.buscarPorId(actualizada.getIdComuna()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(comunaService.eliminar(id), HttpStatus.OK);
    }
}