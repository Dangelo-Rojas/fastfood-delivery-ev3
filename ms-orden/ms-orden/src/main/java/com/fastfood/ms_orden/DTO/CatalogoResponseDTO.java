package com.fastfood.ms_orden.DTO;

import lombok.Data;

@Data
public class CatalogoResponseDTO {
    private Integer idCatalogo;
    private String nombreCatalogo;
    private String descripcionCatalogo;
    private Double precio;
    private String categoria;
    private Boolean disponible;
    private Integer idRestaurante;
}