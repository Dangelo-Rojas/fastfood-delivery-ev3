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

import com.fastfood.ms_delivery.DTO.DeliveryDTO;
import com.fastfood.ms_delivery.model.Delivery;
import com.fastfood.ms_delivery.service.DeliveryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> obtenerTodos() {
        List<DeliveryDTO> deliveries = deliveryService.obtenerTodos();
        if (deliveries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(deliveryService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DeliveryDTO>> obtenerPorEstado(@PathVariable String estado) {
        return new ResponseEntity<>(deliveryService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<List<DeliveryDTO>> obtenerPorConductor(@PathVariable Long idConductor) {
        return new ResponseEntity<>(deliveryService.obtenerPorConductor(idConductor), HttpStatus.OK);
    }

    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<DeliveryDTO> buscarPorOrden(@PathVariable Long idOrden) {
        return new ResponseEntity<>(deliveryService.buscarPorOrden(idOrden), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> guardar(@Valid @RequestBody Delivery delivery) {
        return new ResponseEntity<>(deliveryService.guardar(delivery), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Object> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return new ResponseEntity<>(deliveryService.cambiarEstado(id, estado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(deliveryService.eliminar(id), HttpStatus.OK);
    }
}