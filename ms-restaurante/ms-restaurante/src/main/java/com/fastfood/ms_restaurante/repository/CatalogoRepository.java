package com.fastfood.ms_restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_restaurante.model.Catalogo;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
}