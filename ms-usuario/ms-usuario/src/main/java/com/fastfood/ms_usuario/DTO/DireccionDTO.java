package com.fastfood.ms_usuario.DTO;

import lombok.Data;

@Data
public class DireccionDTO {
    private Long idDireccion;
    private String calle;
    private String numero;
    private String depto;
    private String referencia;
    private Integer idComuna;
    private Long idUsuario;
}