package com.fastfood.ms_usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_usuario.model.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {
}