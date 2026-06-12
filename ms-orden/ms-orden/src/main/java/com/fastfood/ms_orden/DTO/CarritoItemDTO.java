package com.fastfood.ms_orden.DTO;

import lombok.Data;

@Data
public class CarritoItemDTO {
    private Integer idCarritoItem;
    private Integer cantidad;
    private Double precioUnitario;
    private Integer idCarrito;
    private Integer idCatalogo;
}