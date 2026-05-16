package com.nexus.analytics.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.application.mapper.ProductStockViewMapper;
import com.nexus.analytics.domain.exception.AnalyticsServiceException;
import com.nexus.analytics.domain.model.*;
import com.nexus.analytics.domain.repository.ProductStockViewRepository;
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
    private final AnalyticsBroadcasterService analyticsBroadcasterService;

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
                        return existingView;
                    })
                    .orElseGet(() -> productStockViewRepository.save(ProductStockView.builder()
                            .productId(event.id())
                            .sku(event.sku())
                            .productName(event.name())
                            .brandName(event.brandName())
                            .categoryName(event.categoryName())
                            .status(event.status())
                            .slug(event.slug())
                            .price(event.price())
                            .build()));

            return productStockViewMapper.toResponse(view);

        } catch (JsonProcessingException e) {
            throw new AnalyticsServiceException(e, eventType.name());
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
            throw new AnalyticsServiceException(e, eventType.name());
        }

    }

}
