package com.nexus.analytics.application.service;

import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.domain.model.ProductEventType;
import com.nexus.analytics.domain.model.StockEventType;

public interface ProductStockViewService {

    /**
     * Processes and updates product information based on the incoming event.
     *
     * @param payload   The JSON payload containing product data.
     * @param eventType The type of product event.
     * @return The updated product stock view response.
     */
    ProductStockViewResponse upsertProductEvent(String payload, ProductEventType eventType);

    /**
     * Processes and updates stock information based on the incoming event.
     *
     * @param payload   The JSON payload containing stock data.
     * @param eventType The type of stock event.
     * @return The updated product stock view response.
     */
    ProductStockViewResponse upsertStockEvent(String payload, StockEventType eventType);

}
