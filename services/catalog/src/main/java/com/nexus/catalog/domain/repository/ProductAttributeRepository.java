package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.ProductAttribute;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for ProductAttribute persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface ProductAttributeRepository {

    ProductAttribute save(ProductAttribute productAttribute);

    void delete(ProductAttribute productAttribute);

    Optional<ProductAttribute> findById(Long id);

    Optional<ProductAttribute> findByKey(String key);

    List<ProductAttribute> findAllByProduct_Id(Long productId);

    List<ProductAttribute> findAll();

}
