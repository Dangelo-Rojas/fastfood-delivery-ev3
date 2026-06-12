package com.fastfood.ms_orden.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;

    @Column(nullable = false)
    private LocalDateTime fechaOrden;

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE";

    @NotNull(message = "El subtotal es obligatorio")
    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double descuento = 0.0;

    @Column(nullable = false)
    private Double total;

    @NotNull(message = "El carrito es obligatorio")
    @Column(nullable = false)
    private Integer idCarrito;
}