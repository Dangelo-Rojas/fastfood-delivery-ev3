package com.fastfood.ms_usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccion;

    @NotBlank(message = "La calle no puede estar vacía")
    @Column(nullable = false, length = 200)
    private String calle;

    @NotBlank(message = "El número no puede estar vacío")
    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 50)
    private String depto;

    @Column(length = 300)
    private String referencia;

    @Column(nullable = false)
    private Integer idComuna;

    @Column(nullable = false)
    private Long idUsuario;
}