package com.nexus.rag.application.service;

import com.nexus.rag.application.dto.web.response.v1.ProductSearchResponse;

public interface ProductSearchService {

    /**
     * Entry point for executing the RAG search pipeline.
     * Parses the user query, sanitizes it, transforms it, retrieves relevant documents,
     * augments the query with context, and finally generates an answer using the LLM.
     *
     * @param userQuery The initial natural language query provided by the user
     * @return A {@link ProductSearchResponse} containing the generated answer and
     * metadata about the retrieved documents
     */
    ProductSearchResponse search(String userQuery);

}
