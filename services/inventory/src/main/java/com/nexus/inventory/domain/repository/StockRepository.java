package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.Stock;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface StockRepository extends ListCrudRepository<Stock, Long> {

    Optional<Stock> findByProductId(Long productId);

}
