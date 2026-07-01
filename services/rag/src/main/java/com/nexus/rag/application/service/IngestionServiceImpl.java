package com.nexus.rag.application.service;

import com.nexus.rag.application.mapper.ProductDocumentMapper;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.service.ProductHashCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class IngestionServiceImpl implements IngestionService {

    private final ProductDocumentMapper productDocumentMapper;
    private final VectorStore vectorStore;
    private final ProductHashCalculator productHashCalculator;

    @Override
    public void upsertEmbedding(ProductStockView view) {

        String currentHash = productHashCalculator.computeHash(view);

        Document document = productDocumentMapper.toDocument(view, currentHash);
        vectorStore.add(List.of(document));

    }

}
