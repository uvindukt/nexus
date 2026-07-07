package com.nexus.rag.domain.repository;

import com.nexus.rag.domain.model.ProductStockView;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ProductStockViewRepository extends ListCrudRepository<ProductStockView, Long> {

    List<ProductStockView> findByProductIdIn(List<Long> productIds);

}
