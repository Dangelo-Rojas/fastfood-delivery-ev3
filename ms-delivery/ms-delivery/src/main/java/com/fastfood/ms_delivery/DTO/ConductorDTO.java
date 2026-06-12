package com.fastfood.ms_delivery.DTO;

import lombok.Data;

@Data
public class ConductorDTO {
    private Long idConductor;
    private String nombre;
    private String apellido;
    private String telefono;
    private String patenteVehiculo;
    private Boolean disponible;
}