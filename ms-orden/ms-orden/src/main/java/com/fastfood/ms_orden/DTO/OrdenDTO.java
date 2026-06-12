package com.fastfood.ms_orden.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrdenDTO {
    private Long idOrden;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaOrden;

    private String estado;
    private Double subtotal;
    private Double descuento;
    private Double total;
    private Integer idCarrito;
}
