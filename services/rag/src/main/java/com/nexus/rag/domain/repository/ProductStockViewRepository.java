package com.nexus.rag.domain.repository;

import com.nexus.rag.domain.model.ProductStockView;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductStockViewRepository extends ListCrudRepository<ProductStockView, Long> {
}
