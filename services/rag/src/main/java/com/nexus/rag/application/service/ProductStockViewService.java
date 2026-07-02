package com.nexus.rag.application.service;

import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEventType;

public interface ProductStockViewService {

    /**
     * Processes and updates product information based on the incoming event.
     *
     * @param payload   The JSON payload containing product data.
     * @param eventType The type of product event.
     * @throws RagServiceException If an error occurs during JSON processing or persistence.
     */
    void upsertProductEvent(String payload, ProductEventType eventType) throws RagServiceException;

    /**
     * Processes and updates stock information based on the incoming event.
     *
     * @param payload   The JSON payload containing stock data.
     * @param eventType The type of stock event.
     * @throws RagServiceException If an error occurs during JSON processing or persistence.
     */
    void upsertStockEvent(String payload, StockEventType eventType) throws RagServiceException;

}
