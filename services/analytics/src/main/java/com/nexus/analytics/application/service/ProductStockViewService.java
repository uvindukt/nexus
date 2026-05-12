package com.nexus.analytics.application.service;

import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductStockViewService {

    Flux<ProductStockViewResponse> streamProductStockChanges(String productId);

    /**
     * Handles the creation of a new product by initializing a ProductStockView.
     *
     * @param payload The JSON payload representing the ProductEvent.
     * @return A Mono signaling completion.
     */
    Mono<Void> handleProductCreated(String payload);

    /**
     * Updates an existing product's information in the ProductStockView.
     *
     * @param payload The JSON payload representing the ProductEvent.
     * @return A Mono signaling completion, or an error if the product is not found.
     */
    Mono<Void> handleProductUpdated(String payload);

    /**
     * Updates stock levels for an existing product in the ProductStockView.
     *
     * @param payload The JSON payload representing the StockEvent.
     * @return A Mono signaling completion, or an error if the product is not found.
     */
    Mono<Void> handleStockUpsert(String payload);

}
