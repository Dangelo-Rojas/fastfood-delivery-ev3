package com.fastfood.ms_restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_restaurante.model.Promocion;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
}