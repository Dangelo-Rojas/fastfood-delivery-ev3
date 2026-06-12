package com.fastfood.ms_orden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_orden.model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
}