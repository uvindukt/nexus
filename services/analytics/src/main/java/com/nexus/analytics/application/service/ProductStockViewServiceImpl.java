package com.nexus.analytics.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.domain.exception.EntryNotFoundException;
import com.nexus.analytics.domain.model.ProductEvent;
import com.nexus.analytics.domain.model.ProductStockView;
import com.nexus.analytics.domain.model.StockEvent;
import com.nexus.analytics.domain.repository.ProductStockViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockViewServiceImpl implements ProductStockViewService {

    private final ProductStockViewRepository productStockViewRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<ProductStockViewResponse> streamProductStockChanges(String productId) {
        return null;
    }

    @Override
    public Mono<Void> handleProductCreated(String payload) {
        return Mono.fromCallable(() -> objectMapper.readValue(payload, ProductEvent.class))
                .flatMap(event -> {
                    ProductStockView view = new ProductStockView();
                    view.setProductId(event.id());
                    setProductView(event, view);
                    return productStockViewRepository.save(view);
                })
                .then();
    }

    @Override
    public Mono<Void> handleProductUpdated(String payload) {
        return Mono.fromCallable(() -> objectMapper.readValue(payload, ProductEvent.class))
                .flatMap(event -> productStockViewRepository.findById(event.id())
                        .doOnNext(view -> setProductView(event, view))
                        .flatMap(productStockViewRepository::save)
                        .switchIfEmpty(Mono.error(new EntryNotFoundException(event.id())))
                )
                .then();
    }

    @Override
    public Mono<Void> handleStockUpsert(String payload) {
        return Mono.fromCallable(() -> objectMapper.readValue(payload, StockEvent.class))
                .flatMap(event -> productStockViewRepository.findById(event.id())
                        .doOnNext(view -> {
                            view.setAvailableQuantity(event.availableQuantity());
                            view.setReservedQuantity(event.reservedQuantity());
                            view.setTotalQuantity(event.availableQuantity() + event.reservedQuantity());
                            view.setLastUpdated(Instant.now());
                        })
                        .flatMap(productStockViewRepository::save)
                        .switchIfEmpty(Mono.error(new EntryNotFoundException(event.id())))
                )
                .then();
    }

    /**
     * Maps fields from a ProductEvent to a ProductStockView entity.
     *
     * @param event The source product event.
     * @param view  The target view entity.
     */
    private void setProductView(ProductEvent event, ProductStockView view) {
        view.setProductName(event.name());
        view.setSlug(event.slug());
        view.setSku(event.sku());
        view.setPrice(event.price());
        view.setStatus(event.status());
        view.setBrandName(event.brandName());
        view.setCategoryName(event.categoryName());
        view.setLastUpdated(Instant.now());
    }

}
