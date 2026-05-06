package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.Stock;
import org.springframework.data.repository.ListCrudRepository;

public interface StockRepository extends ListCrudRepository<Stock, Long> {



}
