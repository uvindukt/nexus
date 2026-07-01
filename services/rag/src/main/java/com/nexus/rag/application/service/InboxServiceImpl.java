package com.nexus.rag.application.service;

import com.nexus.rag.domain.exception.RagServiceException;
import com.nexus.rag.domain.model.consumer.Inbox;
import com.nexus.rag.domain.model.consumer.ProductEventType;
import com.nexus.rag.domain.model.consumer.StockEventType;
import com.nexus.rag.domain.repository.InboxRepository;
import com.nexus.shared.common.InboxEnvelope;
import com.nexus.shared.common.InboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final InboxRepository inboxRepository;
    private final InboxAuditService inboxAuditService;
    private final ProductStockViewService productStockViewService;

    @Transactional
    @Override
    public void onProductEvent(InboxEnvelope envelope) {

        if (idempotencyCheck(envelope)) return;

        Inbox inbox = createInbox(envelope);
        ProductEventType eventType = ProductEventType.valueOf(envelope.type());

        try {

            if (eventType == ProductEventType.PRODUCT_CREATED || eventType == ProductEventType.PRODUCT_UPDATED) {
                productStockViewService.upsertProductEvent(inbox.getPayload(), eventType);
                inbox.setStatus(InboxStatus.PROCESSED);
                inbox.setProcessedAt(Instant.now());
                inboxRepository.save(inbox);
            } else {
                log.info("Ignored unknown Product event Type - {}, ID - {}", envelope.type(), envelope.id());
                inbox.setStatus(InboxStatus.SKIPPED);
                inbox.setProcessedAt(Instant.now());
                inboxRepository.save(inbox);
            }

        } catch (RagServiceException e) {
            inboxAuditService.saveAsFailed(inbox);
            log.error("Error processing InboxEvent - ID: {}", envelope.id(), e);
            throw e;
        }

    }

    @Transactional
    @Override
    public void onStockEvent(InboxEnvelope envelope) {

        if (idempotencyCheck(envelope)) return;

        Inbox inbox = createInbox(envelope);
        StockEventType eventType = StockEventType.valueOf(envelope.type());

        try {

            if (eventType == StockEventType.STOCK_CREATED || eventType == StockEventType.STOCK_UPDATED) {
                productStockViewService.upsertStockEvent(inbox.getPayload(), eventType);
                inbox.setStatus(InboxStatus.PROCESSED);
                inbox.setProcessedAt(Instant.now());
                inboxRepository.save(inbox);
            } else {
                log.info("Ignored unknown Stock event Type - {}, ID - {}", envelope.type(), envelope.id());
                inbox.setStatus(InboxStatus.SKIPPED);
                inbox.setProcessedAt(Instant.now());
                inboxRepository.save(inbox);
            }

        } catch (RagServiceException e) {
            inboxAuditService.saveAsFailed(inbox);
            log.error("Error processing InboxEvent - ID: {}", envelope.id(), e);
            throw e;
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
