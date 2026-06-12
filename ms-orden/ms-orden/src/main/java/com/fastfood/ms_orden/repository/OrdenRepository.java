package com.fastfood.ms_orden.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_orden.model.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByEstado(String estado);
    Orden findByIdCarrito(Integer idCarrito);
}