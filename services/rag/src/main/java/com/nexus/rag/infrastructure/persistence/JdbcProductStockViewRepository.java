package com.nexus.rag.infrastructure.persistence;

import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcProductStockViewRepository extends JpaRepository<ProductStockView, Long>, ProductStockViewRepository {
}
