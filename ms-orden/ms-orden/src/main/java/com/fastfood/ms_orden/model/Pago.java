package com.fastfood.ms_orden.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @Column(nullable = false)
    private Long idOrden;

    @NotNull(message = "El método de pago es obligatorio")
    @Column(nullable = false)
    private Integer idMetodoPago;

    @NotNull(message = "El monto no puede estar vacío")
    @Column(nullable = false)
    private Integer monto;

    @Column(nullable = false, length = 20)
    private String estado = "APROBADO";

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
}