package com.nexus.analytics.infrastructure.persistence;

import com.nexus.analytics.domain.model.ProductStockView;
import com.nexus.analytics.domain.repository.ProductStockViewRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface R2dbcProductStockViewRepository extends R2dbcRepository<ProductStockView, Long>, ProductStockViewRepository {
}
