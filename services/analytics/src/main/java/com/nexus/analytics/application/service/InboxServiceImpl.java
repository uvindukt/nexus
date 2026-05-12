package com.nexus.analytics.application.service;

import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.model.ProductEventType;
import com.nexus.analytics.domain.model.StockEventType;
import com.nexus.analytics.domain.repository.InboxRepository;
import com.nexus.shared.common.InboxEnvelope;
import com.nexus.shared.common.InboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public Mono<Void> onProductEvent(InboxEnvelope envelope) {
        ProductEventType eventType = ProductEventType.valueOf(envelope.type());
        return processWithIdempotency(envelope, payload -> switch (eventType) {
            case PRODUCT_CREATED -> productStockViewService.handleProductCreated(payload);
            case PRODUCT_UPDATED -> productStockViewService.handleProductUpdated(payload);
        });
    }

    @Transactional
    @Override
    public Mono<Void> onStockEvent(InboxEnvelope envelope) {
        StockEventType eventType = StockEventType.valueOf(envelope.type());
        return processWithIdempotency(envelope, payload -> switch (eventType) {
            case STOCK_CREATED, STOCK_UPDATED -> productStockViewService.handleStockUpsert(payload);
        });
    }

    /**
     * Processes an incoming event with idempotency logic.
     *
     * @param envelope the event envelope containing metadata and payload
     * @param handler  the business logic to execute if the message is new
     * @return a Mono signaling completion
     */
    private Mono<Void> processWithIdempotency(InboxEnvelope envelope, Function<String, Mono<Void>> handler) {

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
                            envelope.payload()
                    );

                    return inboxRepository.save(inbox)
                            .flatMap(saved -> handler.apply(envelope.payload())
                                    .doOnError(e -> log.error("Failed to process event: {}", messageId, e))
                                    .then(Mono.defer(() -> {
                                        saved.setStatus(InboxStatus.PROCESSED.name());
                                        saved.setProcessedAt(Instant.now());
                                        return inboxRepository.save(saved);
                                    })));
                })
                .then();

    }

}
