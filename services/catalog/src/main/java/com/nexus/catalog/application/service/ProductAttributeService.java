package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.ProductAttributeRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductAttributeResponse;

import java.util.List;

public interface ProductAttributeService {

    /**
     * Creates a {@link com.nexus.catalog.domain.model.ProductAttribute} object and persist in database
     *
     * @param productAttributeRequest {@link ProductAttributeRequest} object containing product attribute data
     * @return {@link ProductAttributeResponse} object containing created product attribute data
     */
    ProductAttributeResponse create(ProductAttributeRequest productAttributeRequest);

    /**
     * Updates a {@link com.nexus.catalog.domain.model.ProductAttribute} object with provided data and persist in database
     *
     * @param productAttributeId      Product Attribute ID
     * @param productAttributeRequest {@link ProductAttributeRequest} object containing product attribute data
     * @return {@link ProductAttributeResponse} object containing updated product attribute data
     */
    ProductAttributeResponse update(Long productAttributeId, ProductAttributeRequest productAttributeRequest);

    /**
     * Deletes a {@link com.nexus.catalog.domain.model.ProductAttribute} with provided ID
     *
     * @param productAttributeId Product Attribute ID
     * @return {@link ProductAttributeResponse} object containing product attribute data
     */
    ProductAttributeResponse delete(Long productAttributeId);

    /**
     * Retrieves a {@link com.nexus.catalog.domain.model.ProductAttribute} with provided ID
     *
     * @param productAttributeId Product Attribute ID
     * @return {@link ProductAttributeResponse} object containing product attribute data
     */
    ProductAttributeResponse get(Long productAttributeId);

    /**
     * Retrieves a {@link com.nexus.catalog.domain.model.ProductAttribute} with provided Key
     *
     * @param productAttributeKey Product Attribute Key
     * @param productId           Product ID
     * @return {@link ProductAttributeResponse} object containing product attribute data
     */
    ProductAttributeResponse get(String productAttributeKey, Long productId);

    /**
     * Retrieves all {@link com.nexus.catalog.domain.model.ProductAttribute} objects
     *
     * @return {@link List<ProductAttributeResponse>} object containing all product attributes
     */
    List<ProductAttributeResponse> getAll();

}
