package com.fastfood.ms_delivery.model;

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
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDelivery;

    @Column(nullable = false)
    private LocalDateTime horaSalida;

    @Column
    private LocalDateTime horaLlegada;

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE";

    @NotNull(message = "La orden es obligatoria")
    @Column(nullable = false)
    private Long idOrden;

    @NotNull(message = "El conductor es obligatorio")
    @Column(nullable = false)
    private Long idConductor;
}
