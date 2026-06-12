package com.fastfood.ms_restaurante.DTO;

import lombok.Data;

@Data
public class CatalogoDTO {
    private Integer idCatalogo;
    private String nombreCatalogo;
    private String descripcionCatalogo;
    private Double precio;
    private String categoria;
    private Boolean disponible;
    private Integer idRestaurante;
}