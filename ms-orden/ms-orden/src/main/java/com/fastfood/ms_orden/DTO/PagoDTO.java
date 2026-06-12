package com.fastfood.ms_orden.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PagoDTO {
    private Long idPago;
    private Long idOrden;
    private Integer idMetodoPago;
    private Integer monto;
    private String estado;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaPago;
}