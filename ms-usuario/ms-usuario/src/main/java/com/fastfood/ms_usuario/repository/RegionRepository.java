package com.fastfood.ms_usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_usuario.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}