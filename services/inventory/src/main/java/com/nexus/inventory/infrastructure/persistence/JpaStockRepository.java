package com.nexus.inventory.infrastructure.persistence;

import com.nexus.inventory.domain.model.Stock;
import com.nexus.inventory.domain.repository.StockRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaStockRepository extends JpaRepository<Stock, Long>, StockRepository {

    Optional<Stock> findByProductId(Long productId);

}
