package com.nexus.rag.application.service;

import com.nexus.rag.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEventType;

public interface ProductStockViewService {

    /**
     * Processes and updates product information based on the incoming event.
     *
     * @param payload   The JSON payload containing product data.
     * @param eventType The type of product event.
     * @return The updated product stock view response.
     * @throws RagServiceException If an error occurs during JSON processing or persistence.
     */
    ProductStockViewResponse upsertProductEvent(String payload, ProductEventType eventType) throws RagServiceException;

    /**
     * Processes and updates stock information based on the incoming event.
     *
     * @param payload   The JSON payload containing stock data.
     * @param eventType The type of stock event.
     * @return The updated product stock view response.
     * @throws RagServiceException If an error occurs during JSON processing or persistence.
     */
    ProductStockViewResponse upsertStockEvent(String payload, StockEventType eventType) throws RagServiceException;

}
