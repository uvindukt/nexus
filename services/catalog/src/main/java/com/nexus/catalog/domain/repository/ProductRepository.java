package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for Product persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);

    List<Product> findBySkuOrSlug(String sku, String slug);

    List<Product> findAll();

}
