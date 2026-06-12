package com.fastfood.ms_orden.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrito;

    @NotBlank(message = "El estado del carrito es obligatorio")
    @Column(nullable = false, length = 100)
    private String estado;

    @NotNull(message = "El usuario es obligatorio")
    @Column(nullable = false)
    private Long idUsuario;

    @NotNull(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private Integer idDireccion;
}