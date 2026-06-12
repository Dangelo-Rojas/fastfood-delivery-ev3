package com.fastfood.ms_restaurante.DTO;

import lombok.Data;

@Data
public class RestauranteDTO {
    private Integer id_restaurante;
    private String nombre_restaurante;
    private String direccion_restaurante;
    private String telefono_restaurante;
    private String correo_restaurante;
}