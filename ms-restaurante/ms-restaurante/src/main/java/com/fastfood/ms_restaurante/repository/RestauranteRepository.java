package com.fastfood.ms_restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_restaurante.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {
}
