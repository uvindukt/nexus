package com.nexus.rag.application.service;

import com.nexus.rag.domain.model.ProductStockView;

public interface IngestionService {

    /**
     * Converts a product stock view into a vector representation and upserts it into the vector store.
     *
     * @param productStockView The product data to be indexed.
     */
    void upsertEmbedding(ProductStockView productStockView);

}
