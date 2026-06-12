package com.fastfood.ms_usuario.DTO;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}
