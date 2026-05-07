package com.nexus.inventory.application.service;

import com.nexus.inventory.application.dto.web.request.v1.StockRequest;
import com.nexus.inventory.application.dto.web.response.v1.StockResponse;

import java.util.List;

public interface StockService {

    /**
     * Creates a new stock entry or updates the available quantity of an existing one.
     *
     * @param productId the ID of the product
     * @param request   the stock request containing the new quantity
     * @return the updated stock information
     */
    StockResponse upsertStock(Long productId, StockRequest request);

    /**
     * Increments the available quantity of an existing stock entry.
     *
     * @param productId the ID of the product
     * @param request   the stock request containing the quantity to add
     * @return the updated stock information
     */
    StockResponse addToStock(Long productId, StockRequest request);

    /**
     * Retrieves the stock information for a specific product.
     *
     * @param productId the ID of the product
     * @return the stock information
     */
    StockResponse get(Long productId);

    /**
     * Retrieves stock information for a selection of products.
     *
     * @param productIds an array of product IDs
     * @return a list of stock information for the found products
     */
    List<StockResponse> getSelected(Long[] productIds);

}
