package com.nexus.rag.application.service;

import com.nexus.rag.application.mapper.ProductDocumentMapper;
import com.nexus.rag.domain.exception.IngestionPipelineException;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductEmbeddingRepository;
import com.nexus.rag.domain.service.ProductHashCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.nexus.rag.infrastructure.persistence.constant.RepositoryConstants.QDRANT_PRODUCT_EMBEDDING_REPOSITORY;

@Slf4j
@Service
class IngestionServiceImpl implements IngestionService {

    private final ProductDocumentMapper productDocumentMapper;
    private final VectorStore vectorStore;
    private final ProductHashCalculator productHashCalculator;
    private final ProductEmbeddingRepository productEmbeddingRepository;

    public IngestionServiceImpl(
            ProductDocumentMapper productDocumentMapper,
            VectorStore vectorStore,
            ProductHashCalculator productHashCalculator,
            @Qualifier(QDRANT_PRODUCT_EMBEDDING_REPOSITORY) ProductEmbeddingRepository productEmbeddingRepository
    ) {
        this.productDocumentMapper = productDocumentMapper;
        this.vectorStore = vectorStore;
        this.productHashCalculator = productHashCalculator;
        this.productEmbeddingRepository = productEmbeddingRepository;
    }

    @Override
    public void upsertEmbedding(ProductStockView view) {

        try {

            String currentHash = productHashCalculator.computeHash(view);

            Optional<String> storedHash = productEmbeddingRepository.findContentHashByProductId(view.getProductId());

            if (storedHash.isPresent()) {
                if (storedHash.get().equals(currentHash)) {
                    log.debug("Content hash unchanged for productId {}, skipping re-embedding", view.getProductId());
                    return;
                }
                deleteExistingDocument(view.getProductId());
            }

            Document document = productDocumentMapper.toDocument(view, currentHash);
            vectorStore.add(List.of(document));

            log.info("Ingested product with productId - {}", view.getProductId());

        } catch (RuntimeException e) {
            throw new IngestionPipelineException(e, "Unknown failure");
        }

    }

    private void deleteExistingDocument(Long productId) {
        FilterExpressionBuilder filterExpressionBuilder = new FilterExpressionBuilder();
        Filter.Expression filter = filterExpressionBuilder.eq("productId", productId.toString()).build();
        vectorStore.delete(filter);
    }

}
