package com.nexus.rag.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.rag.domain.exception.IngestionPipelineException;
import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.EmbeddingStatus;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.model.consumer.ProductEvent;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEvent;
import com.nexus.rag.domain.model.consumer.StockEventType;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockViewServiceImpl implements ProductStockViewService {

    private final ProductStockViewRepository productStockViewRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void upsertProductEvent(String payload, ProductEventType eventType) {

        try {

            ProductEvent event = objectMapper.readValue(payload, ProductEvent.class);

            productStockViewRepository.findById(event.id())
                    .map(existingView -> {
                        existingView.setProductName(event.name());
                        existingView.setSlug(event.slug());
                        existingView.setSku(event.sku());
                        existingView.setPrice(event.price());
                        existingView.setStatus(event.status());
                        existingView.setBrandName(event.brandName());
                        existingView.setCategoryName(event.categoryName());
                        existingView.setDescription(event.description());
                        return existingView;
                    })
                    .orElseGet(() -> productStockViewRepository.save(ProductStockView.builder()
                            .productId(event.id())
                            .sku(event.sku())
                            .productName(event.name())
                            .brandName(event.brandName())
                            .categoryName(event.categoryName())
                            .description(event.description())
                            .status(event.status())
                            .slug(event.slug())
                            .price(event.price())
                            .build()));

        } catch (JsonProcessingException e) {
            throw new RagServiceException(e, eventType.name());
        }

    }

    @Transactional
    @Override
    public void upsertStockEvent(String payload, StockEventType eventType) {

        try {

            StockEvent event = objectMapper.readValue(payload, StockEvent.class);

            productStockViewRepository.findById(event.id())
                    .map(existingView -> {
                        existingView.setAvailableQuantity(event.availableQuantity());
                        existingView.setReservedQuantity(event.reservedQuantity());
                        return existingView;
                    })
                    .orElseGet(() -> productStockViewRepository.save(ProductStockView.builder()
                            .productId(event.id())
                            .availableQuantity(event.availableQuantity())
                            .reservedQuantity(event.reservedQuantity())
                            .build()));

        } catch (JsonProcessingException e) {
            throw new RagServiceException(e, eventType.name());
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateEmbeddingStatus(Long productId, EmbeddingStatus embeddingStatus) throws RagServiceException {

        ProductStockView productStockView = productStockViewRepository
                .findById(productId)
                .orElseThrow(() -> new IngestionPipelineException("Product not found"));
        productStockView.setEmbeddingStatus(embeddingStatus);
        productStockViewRepository.save(productStockView);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markEmbeddingOpFailed(Long productId) throws RagServiceException {

        ProductStockView productStockView = productStockViewRepository
                .findById(productId)
                .orElseThrow(() -> new IngestionPipelineException("Product not found"));
        productStockView.setEmbeddingStatus(EmbeddingStatus.FAILED);
        productStockViewRepository.save(productStockView);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<ProductStockView> claimProductBatchByEmbeddingStatus(EmbeddingStatus embeddingStatus, Integer batchSize) {

        List<ProductStockView> productStockViews = productStockViewRepository.findByEmbeddingStatus(
                embeddingStatus,
                PageRequest.of(
                        0,
                        batchSize,
                        Sort.by(Sort.Direction.ASC, "productId")
                )
        );
        productStockViews.forEach(productStockView -> productStockView.setEmbeddingStatus(EmbeddingStatus.PROCESSING));
        return productStockViews;

    }

}
