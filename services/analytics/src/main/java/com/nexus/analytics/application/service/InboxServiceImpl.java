package com.nexus.analytics.application.service;

import com.nexus.analytics.application.dto.web.event.v1.ProductStockViewEvent;
import com.nexus.analytics.application.dto.web.event.v1.SseEnvelope;
import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.domain.exception.AnalyticsServiceException;
import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.model.ProductEventType;
import com.nexus.analytics.domain.model.StockEventType;
import com.nexus.analytics.domain.repository.InboxRepository;
import com.nexus.shared.common.InboxEnvelope;
import com.nexus.shared.common.InboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final ProductStockViewService productStockViewService;
    private final InboxRepository inboxRepository;
    private final InboxAuditService inboxAuditService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void onProductEvent(InboxEnvelope envelope) {

        processEvent(envelope, payload -> {
            ProductEventType eventType = ProductEventType.valueOf(envelope.type());
            return switch (eventType) {
                case PRODUCT_CREATED, PRODUCT_UPDATED -> productStockViewService.upsertProductEvent(payload, eventType);
                default -> {
                    log.info("Ignored unknown Product event Type - {}, ID - {}", envelope.type(), envelope.id());
                    yield null;
                }
            };
        });

    }

    @Transactional
    @Override
    public void onStockEvent(InboxEnvelope envelope) {

        processEvent(envelope, payload -> {
            StockEventType eventType = StockEventType.valueOf(envelope.type());
            return switch (eventType) {
                case STOCK_CREATED, STOCK_UPDATED -> productStockViewService.upsertStockEvent(payload, eventType);
                default -> {
                    log.info("Ignored unknown Stock event Type - {}, ID - {}", envelope.type(), envelope.id());
                    yield null;
                }
            };
        });

    }

    /**
     * Wrapper for event processors
     *
     * @param envelope  {@link InboxEnvelope} whole message
     * @param processor specific event processor
     */
    private void processEvent(InboxEnvelope envelope, Function<String, ProductStockViewResponse> processor) {

        if (idempotencyCheck(envelope)) return;

        Inbox inbox = createInbox(envelope);
        ProductStockViewResponse response;

        try {
            response = processor.apply(envelope.payload());
        } catch (AnalyticsServiceException e) {
            inboxAuditService.saveAsFailed(inbox);
            log.error("Error processing InboxEvent - ID: {}", envelope.id(), e);
            throw e;
        }

        if (response != null) {
            inbox.setStatus(InboxStatus.PROCESSED);
            inbox.setProcessedAt(Instant.now());
            inboxRepository.save(inbox);
            // for Transactional Event Listener to activate after this tx commits
            SseEnvelope sseEnvelope = new SseEnvelope(envelope.id(), envelope.aggregateId(), envelope.aggregateType(), envelope.type(), response);
            eventPublisher.publishEvent(new ProductStockViewEvent(sseEnvelope));
        } else {
            inbox.setStatus(InboxStatus.SKIPPED);
            inbox.setProcessedAt(Instant.now());
            inboxRepository.save(inbox);
        }

    }

    /**
     * Will return true if a duplicate is found
     *
     * @param inboxEnvelope Message
     */
    private boolean idempotencyCheck(InboxEnvelope inboxEnvelope) {

        try {

            if (inboxRepository.existsById(UUID.fromString(inboxEnvelope.id()))) {
                log.warn("Message already exists ID - {}", inboxEnvelope.id());
                return true;
            }

            return false;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID", e);
        }

    }

    private Inbox createInbox(InboxEnvelope envelope) {

        Inbox inbox = Inbox.builder()
                .id(UUID.fromString(envelope.id()))
                .type(envelope.type())
                .status(InboxStatus.PENDING)
                .aggregateId(envelope.aggregateId())
                .aggregateType(envelope.aggregateType())
                .payload(envelope.payload())
                .build();
        return inboxRepository.save(inbox);

    }

}
