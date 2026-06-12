package com.fastfood.ms_orden.DTO;

import lombok.Data;

@Data
public class CarritoDTO {
    private Integer idCarrito;
    private String estado;
    private Long idUsuario;
    private Integer idDireccion;
}