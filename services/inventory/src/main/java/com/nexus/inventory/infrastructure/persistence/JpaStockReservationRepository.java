package com.nexus.inventory.infrastructure.persistence;

import com.nexus.inventory.domain.model.StockReservation;
import com.nexus.inventory.domain.repository.StockReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStockReservationRepository extends JpaRepository<StockReservation, Long>, StockReservationRepository {
}
