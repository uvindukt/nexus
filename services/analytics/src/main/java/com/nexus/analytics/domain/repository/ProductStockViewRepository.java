package com.nexus.analytics.domain.repository;

import com.nexus.analytics.domain.model.ProductStockView;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@NoRepositoryBean
public interface ProductStockViewRepository extends ReactiveCrudRepository<ProductStockView, Long> {
}
