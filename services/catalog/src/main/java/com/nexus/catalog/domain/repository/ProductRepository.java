package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for Product persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
@NoRepositoryBean
public interface ProductRepository extends ListCrudRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findBySkuOrSlug(String sku, String slug);

}
