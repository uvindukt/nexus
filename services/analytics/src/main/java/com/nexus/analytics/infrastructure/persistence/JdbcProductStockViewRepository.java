package com.nexus.analytics.infrastructure.persistence;

import com.nexus.analytics.domain.model.ProductStockView;
import com.nexus.analytics.domain.repository.ProductStockViewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcProductStockViewRepository extends JpaRepository<ProductStockView, Long>, ProductStockViewRepository {
}
