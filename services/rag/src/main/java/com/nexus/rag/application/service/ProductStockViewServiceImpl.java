package com.nexus.rag.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.rag.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.rag.application.mapper.ProductStockViewMapper;
import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.model.consumer.ProductEvent;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEvent;
import com.nexus.rag.domain.model.consumer.StockEventType;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockViewServiceImpl implements ProductStockViewService {

    private final ProductStockViewRepository productStockViewRepository;
    private final ObjectMapper objectMapper;
    private final ProductStockViewMapper productStockViewMapper;

    @Transactional
    @Override
    public ProductStockViewResponse upsertProductEvent(String payload, ProductEventType eventType) {

        try {

            ProductEvent event = objectMapper.readValue(payload, ProductEvent.class);

            ProductStockView view = productStockViewRepository.findById(event.id())
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

            return productStockViewMapper.toResponse(view);

        } catch (JsonProcessingException e) {
            throw new RagServiceException(e, eventType.name());
        }

    }

    @Transactional
    @Override
    public ProductStockViewResponse upsertStockEvent(String payload, StockEventType eventType) {

        try {

            StockEvent event = objectMapper.readValue(payload, StockEvent.class);

            ProductStockView view = productStockViewRepository.findById(event.id())
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

            return productStockViewMapper.toResponse(view);

        } catch (JsonProcessingException e) {
            throw new RagServiceException(e, eventType.name());
        }

    }

}
