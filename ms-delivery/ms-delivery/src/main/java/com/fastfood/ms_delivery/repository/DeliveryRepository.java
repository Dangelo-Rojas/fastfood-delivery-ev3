package com.fastfood.ms_delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfood.ms_delivery.model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByEstado(String estado);
    Delivery findByIdOrden(Long idOrden);
    List<Delivery> findByIdConductor(Long idConductor);
}
