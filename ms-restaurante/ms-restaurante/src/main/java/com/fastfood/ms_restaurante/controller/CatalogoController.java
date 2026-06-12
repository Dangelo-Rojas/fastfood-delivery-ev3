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

import com.fastfood.ms_restaurante.DTO.CatalogoDTO;
import com.fastfood.ms_restaurante.model.Catalogo;
import com.fastfood.ms_restaurante.service.CatalogoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/catalogos")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping
    public ResponseEntity<List<CatalogoDTO>> obtenerTodos() {
        List<CatalogoDTO> catalogos = catalogoService.obtenerTodos();
        if (catalogos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(catalogos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(catalogoService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CatalogoDTO> crear(@Valid @RequestBody Catalogo catalogo) {
        return new ResponseEntity<>(catalogoService.guardar(catalogo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoDTO> actualizar(@PathVariable Integer id, @RequestBody Catalogo catalogo) {
        return new ResponseEntity<>(catalogoService.actualizar(id, catalogo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return new ResponseEntity<>(catalogoService.eliminar(id), HttpStatus.OK);
    }
}