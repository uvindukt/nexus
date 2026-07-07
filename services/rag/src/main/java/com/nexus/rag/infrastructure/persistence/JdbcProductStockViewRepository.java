package com.nexus.rag.infrastructure.persistence;

import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JdbcProductStockViewRepository extends JpaRepository<ProductStockView, Long>, ProductStockViewRepository {

    List<ProductStockView> findByProductIdIn(List<Long> productId);

}
