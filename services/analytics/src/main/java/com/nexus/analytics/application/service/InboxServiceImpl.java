package com.nexus.analytics.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.application.mapper.ProductStockViewMapper;
import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.model.ProductEventType;
import com.nexus.analytics.domain.model.StockEventType;
import com.nexus.analytics.domain.repository.InboxRepository;
import com.nexus.shared.common.InboxEnvelope;
import com.nexus.shared.common.InboxStatus;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final ProductStockViewService productStockViewService;
    private final InboxRepository inboxRepository;
    private final AnalyticsBroadcaster analyticsBroadcaster;
    private final ObjectMapper objectMapper;
    private final ProductStockViewMapper productStockViewMapper;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Void> onProductEvent(InboxEnvelope envelope) {
        ProductEventType eventType = ProductEventType.valueOf(envelope.type());
        return processWithIdempotency(envelope, payload -> switch (eventType) {
            case PRODUCT_CREATED -> productStockViewService.handleProductCreated(payload);
            case PRODUCT_UPDATED -> productStockViewService.handleProductUpdated(payload);
        }).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> onStockEvent(InboxEnvelope envelope) {
        StockEventType eventType = StockEventType.valueOf(envelope.type());
        return processWithIdempotency(envelope, payload -> switch (eventType) {
            case STOCK_CREATED, STOCK_UPDATED -> productStockViewService.handleStockUpsert(payload);
        }).as(transactionalOperator::transactional);
    }

    /**
     * Processes an incoming event with idempotency logic.
     *
     * @param envelope the event envelope containing metadata and payload
     * @param handler  the business logic to execute if the message is new
     * @return a Mono signaling completion
     */
    private Mono<Void> processWithIdempotency(InboxEnvelope envelope, Function<String, Mono<ProductStockViewResponse>> handler) {

        UUID messageId = UUID.fromString(envelope.id());

        return inboxRepository.existsById(messageId)
                .flatMap(exists -> {
                    if (exists) {
                        log.info("Duplicate message ignored: {}", messageId);
                        return Mono.empty();
                    }

                    Inbox inbox = Inbox.of(
                            messageId,
                            envelope.aggregateType(),
                            envelope.aggregateId(),
                            envelope.type(),
                            Json.of(envelope.payload())
                    );

                    return inboxRepository.save(inbox)
                            .flatMap(saved -> handler.apply(envelope.payload())
                                    .flatMap(response -> {
                                        // 1. Mark as processed
                                        saved.setStatus(InboxStatus.PROCESSED.name());
                                        saved.setProcessedAt(Instant.now());

                                        return inboxRepository.save(saved)
                                                .flatMap(savedInbox -> Mono.fromCallable(() -> objectMapper.writeValueAsString(savedInbox)))
                                                // 2. Shout to Redis Backplane
                                                .doOnNext(json -> {
                                                    try {
                                                        analyticsBroadcaster.broadcast(json);
                                                    } catch (Exception e) {
                                                        log.error("Failed to broadcast analytics event", e);
                                                    }
                                                })
                                                .then();
                                    })
                                    .doOnError(e -> log.error("Failed to process event: {}", messageId, e)));
                })
                .doOnError(e -> log.error("Failed to process event: {}", messageId, e))
                .then();

    }

}
