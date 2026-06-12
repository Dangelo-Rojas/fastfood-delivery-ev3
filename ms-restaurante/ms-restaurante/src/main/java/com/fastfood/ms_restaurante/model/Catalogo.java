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
@Table(name = "catalogo")
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCatalogo;

    @NotBlank(message = "El nombre del catálogo es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombreCatalogo;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 100)
    private String descripcionCatalogo;

    @NotNull(message = "El precio es obligatorio")
    @Column(nullable = false)
    private Double precio;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false, length = 100)
    private String categoria;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Column(nullable = false)
    private Boolean disponible;

    @NotNull(message = "El restaurante es obligatorio")
    @Column(nullable = false)
    private Integer idRestaurante;
}