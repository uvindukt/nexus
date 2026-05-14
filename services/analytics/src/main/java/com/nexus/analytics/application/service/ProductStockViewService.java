package com.nexus.analytics.application.service;

import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductStockViewService {

    Flux<ProductStockViewResponse> streamActiveStockChanges();

    /**
     * Handles the creation of a new product by initializing a ProductStockView.
     *
     * @param payload The JSON payload representing the ProductEvent.
     * @return A Mono containing the created ProductStockViewResponse.
     */
    Mono<ProductStockViewResponse> handleProductCreated(String payload);

    /**
     * Updates an existing product's information in the ProductStockView.
     *
     * @param payload The JSON payload representing the ProductEvent.
     * @return A Mono containing the updated ProductStockViewResponse, or an error if not found.
     */
    Mono<ProductStockViewResponse> handleProductUpdated(String payload);

    /**
     * Updates stock levels for an existing product in the ProductStockView.
     *
     * @param payload The JSON payload representing the StockEvent.
     * @return A Mono containing the updated ProductStockViewResponse, or an error if not found.
     */
    Mono<ProductStockViewResponse> handleStockUpsert(String payload);

}
