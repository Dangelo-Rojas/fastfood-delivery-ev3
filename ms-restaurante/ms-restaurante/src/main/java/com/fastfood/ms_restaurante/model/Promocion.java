package com.fastfood.ms_restaurante.model;

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
@Table(name = "promocion")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPromocion;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombrePromocion;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 100)
    private String descripcionPromocion;

    @NotNull(message = "El precio de la promoción es obligatorio")
    @Column(nullable = false)
    private Double precioPromocion;

    @NotNull(message = "El catálogo es obligatorio")
    @Column(nullable = false)
    private Integer idCatalogo;
}