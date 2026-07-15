package com.nexus.rag.application.service;

import com.nexus.rag.domain.exception.IngestionPipelineException;
import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.EmbeddingStatus;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEventType;

import java.util.List;

public interface ProductStockViewService {

    /**
     * Processes and updates product information based on the incoming event.
     * This method upserts (inserts or updates) product data into the repository.
     *
     * @param payload   The JSON payload containing product data.
     * @param eventType The type of product event (e.g., CREATE, UPDATE).
     * @throws RagServiceException If an error occurs during JSON processing or persistence.
     */
    void upsertProductEvent(String payload, ProductEventType eventType) throws RagServiceException;

    /**
     * Processes and updates stock information based on the incoming event.
     * This method upserts (inserts or updates) stock data into the repository.
     *
     * @param payload   The JSON payload containing stock data.
     * @param eventType The type of stock event (e.g., INCREASE, DECREASE).
     * @throws RagServiceException If an error occurs during JSON processing or persistence.
     */
    void upsertStockEvent(String payload, StockEventType eventType) throws RagServiceException;

    /**
     * Updates the embedding status for a specific product.
     * This operation is transactional and ensures data consistency.
     *
     * @param productId       The ID of the product to update.
     * @param embeddingStatus The new embedding status (e.g., PENDING, PROCESSING).
     * @throws IngestionPipelineException If an error occurs during the update or if the product is not found.
     */
    void updateEmbeddingStatus(Long productId, EmbeddingStatus embeddingStatus) throws IngestionPipelineException;

    /**
     * Marks the embedding operation as failed for a specific product.
     * This operation is transactional and ensures data consistency.
     *
     * @param productId The ID of the product to mark as failed.
     * @throws IngestionPipelineException If an error occurs during the update or if the product is not found.
     */
    void markEmbeddingOpFailed(Long productId) throws IngestionPipelineException;

    /**
     * Marks the embedding operation as dead for a specific product.
     * This operation is transactional and ensures data consistency.
     *
     * @param productId The ID of the product to mark as dead.
     * @throws IngestionPipelineException If an error occurs during the update or if the product is not found.
     */
    void markEmbeddingOpDead(Long productId) throws IngestionPipelineException;

    /**
     * Claims a batch of products with a specific embedding status for processing.
     * This method retrieves products with the specified embedding status and marks them as claimed.
     * It ensures that the batchSize does not exceed system limits and returns an empty list if no products are available.
     *
     * @param embeddingStatus The embedding status to filter by (e.g., PENDING, FAILED).
     * @param batchSize       The maximum number of products to claim in this batch (must be positive).
     * @return A list of product stock views that have been successfully claimed.
     * @throws IllegalArgumentException If batchSize is less than or equal to zero.
     */
    List<ProductStockView> claimProductBatchByEmbeddingStatus(EmbeddingStatus embeddingStatus, Integer batchSize);

}
