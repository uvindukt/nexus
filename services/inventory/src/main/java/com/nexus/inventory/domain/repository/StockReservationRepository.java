package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.StockReservation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface StockReservationRepository extends ListCrudRepository<StockReservation, Long> {
}
