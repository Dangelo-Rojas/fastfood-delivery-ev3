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

import com.fastfood.ms_usuario.DTO.RegionDTO;
import com.fastfood.ms_usuario.model.Region;
import com.fastfood.ms_usuario.service.RegionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodas() {
        List<RegionDTO> regiones = regionService.obtenerTodas();
        if (regiones.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(regiones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(regionService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RegionDTO> crear(@Valid @RequestBody Region region) {
        Region guardada = regionService.guardar(region);
        return new ResponseEntity<>(regionService.buscarPorId(guardada.getIdRegion()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> actualizar(@PathVariable Long id, @RequestBody Region region) {
        Region actualizada = regionService.actualizar(id, region);
        return new ResponseEntity<>(regionService.buscarPorId(actualizada.getIdRegion()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(regionService.eliminar(id), HttpStatus.OK);
    }
}