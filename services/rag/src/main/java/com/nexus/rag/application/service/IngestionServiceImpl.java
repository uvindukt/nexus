package com.nexus.rag.application.service;

import com.nexus.rag.application.mapper.ProductDocumentMapper;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductEmbeddingRepository;
import com.nexus.rag.domain.service.ProductHashCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class IngestionServiceImpl implements IngestionService {

    private final ProductDocumentMapper productDocumentMapper;
    private final VectorStore vectorStore;
    private final ProductHashCalculator productHashCalculator;
    private final ProductEmbeddingRepository productEmbeddingRepository;

    @Override
    public void upsertEmbedding(ProductStockView view) {

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

    }

    private void deleteExistingDocument(Long productId) {
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression filter = b.eq("productId", productId.toString()).build();
        vectorStore.delete(filter);
    }

}
