package com.nexus.rag.application.service;

import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.EmbeddingStatus;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEventType;

import java.util.List;

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

    /**
     * Updates the embedding status for a specific product.
     *
     * @param productId       The ID of the product to update.
     * @param embeddingStatus The new embedding status.
     * @throws RagServiceException If an error occurs during the update.
     */
    void updateEmbeddingStatus(Long productId, EmbeddingStatus embeddingStatus) throws RagServiceException;

    /**
     * Marks the embedding operation as failed for a specific product.
     *
     * @param productId The ID of the product to mark as failed.
     * @throws RagServiceException If an error occurs during the update.
     */
    void markEmbeddingOpFailed(Long productId) throws RagServiceException;

    /**
     * Claims a batch of products with a specific embedding status for processing.
     *
     * @param embeddingStatus The embedding status to filter by.
     * @param batchSize       The maximum number of products to claim in this batch.
     * @return A list of product stock views that have been claimed.
     */
    List<ProductStockView> claimProductBatchByEmbeddingStatus(EmbeddingStatus embeddingStatus, Integer batchSize);

}
