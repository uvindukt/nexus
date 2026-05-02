package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.ProductRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import com.nexus.catalog.domain.model.Product;

import java.util.List;

public interface ProductService {

    /**
     * Creates a {@link Product} object and persist in database
     *
     * @param productRequest {@link ProductRequest} object containing product data
     * @return {@link ProductResponse} object containing created product data
     */
    ProductResponse create(ProductRequest productRequest);

    /**
     * Updates a {@link Product} object with provided data and persist in database
     *
     * @param productId      Product ID
     * @param productRequest {@link ProductRequest} object containing product data
     * @return {@link ProductResponse} object containing updated product data
     */
    ProductResponse update(Long productId, ProductRequest productRequest);

    /**
     * Deactivates a {@link Product} with provided ID
     *
     * @param productId Product ID
     * @return {@link ProductResponse} object containing product data
     */
    ProductResponse delete(Long productId);

    /**
     * Activates a {@link Product} with provided ID
     *
     * @param productId Product ID
     * @return {@link ProductResponse} object containing product data
     */
    ProductResponse activate(Long productId);

    /**
     * Retrieves a {@link Product} with provided ID
     *
     * @param productId Product ID
     * @return {@link ProductResponse} object containing product data
     */
    ProductResponse get(Long productId);

    /**
     * Retrieves a {@link Product} with provided SKU
     *
     * @param sku Product SKU
     * @return {@link ProductResponse} object containing product data
     */
    ProductResponse get(String sku);

    /**
     * Retrieves all {@link Product} objects
     *
     * @return List of {@link ProductResponse} objects containing all products
     */
    List<ProductResponse> getAll();

}
