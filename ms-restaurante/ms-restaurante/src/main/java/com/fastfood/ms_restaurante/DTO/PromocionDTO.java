package com.fastfood.ms_restaurante.DTO;

import lombok.Data;

@Data
public class PromocionDTO {
    private Integer idPromocion;
    private String nombrePromocion;
    private String descripcionPromocion;
    private Double precioPromocion;
    private Integer idCatalogo;
}