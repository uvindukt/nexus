package com.nexus.catalog.domain.repository;

import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;

import java.util.Optional;

public interface CachedProductRepository {

    /**
     * Finds a product by its unique identifier, checking the cache first.
     *
     * @param id the unique identifier of the product
     * @return an Optional containing the product if found, or empty if not
     */
    Optional<ProductResponse> findById(Long id);

    /**
     * Finds a product by its stock keeping unit (SKU), checking the cache first.
     *
     * @param sku the stock keeping unit of the product
     * @return an Optional containing the product if found, or empty if not
     */
    Optional<ProductResponse> findBySku(String sku);

}
