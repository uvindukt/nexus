package com.nexus.inventory.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.inventory.application.mapper.InboxMapper;
import com.nexus.inventory.domain.model.*;
import com.nexus.inventory.domain.repository.InboxArchiveRepository;
import com.nexus.inventory.domain.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private static final String PRODUCT_CREATED = "PRODUCT_CREATED";
    private static final String PRODUCT_UPDATED = "PRODUCT_UPDATED";

    private final StockService stockService;
    private final ObjectMapper objectMapper;
    private final InboxMapper inboxMapper;
    private final InboxRepository inboxRepository;
    private final InboxArchiveRepository inboxArchiveRepository;

    @Transactional
    @Override
    public void onStockEvent(InboxEnvelope inboxEnvelope) {

        if (idempotencyCheck(inboxEnvelope)) {
            return;
        }

        try {

            switch (inboxEnvelope.type()) {

                case PRODUCT_CREATED -> {
                    stockService.addStock(Long.valueOf(inboxEnvelope.aggregateId()));
                }
                case PRODUCT_UPDATED -> {
                    var payload = objectMapper.readValue(inboxEnvelope.payload(), ProductPayload.class);
                    stockService.updateStock(Long.valueOf(inboxEnvelope.aggregateId()), payload.price());
                }
                default ->
                        log.info("Ignored unknown event Type - {}, ID - {}", inboxEnvelope.type(), inboxEnvelope.id());

            }

            // Saving to Inbox for future idempotency checks
            Inbox inbox = Inbox.builder()
                    .id(UUID.fromString(inboxEnvelope.id()))
                    .type(InboxEventType.valueOf(inboxEnvelope.type()))
                    .processedAt(Instant.now())
                    .lastAttemptedAt(Instant.now())
                    .status(InboxStatus.PROCESSED)
                    .build();
            inboxMapper.toInbox(inboxEnvelope, inbox);
            inboxRepository.save(inbox);

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize payload for message ID - {}", inboxEnvelope.id(), e);
            throw new RuntimeException("Deserialization failed", e);
        }

    }

    @Override
    public void archive(Integer maxRetries, Integer batchSize) {

        List<Inbox> inboxes = inboxRepository.findByStatusAndRetryCountLessThan(InboxStatus.PENDING, maxRetries, PageRequest.of(0, batchSize));
        var archives = inboxMapper.toArchives(inboxes);

        inboxArchiveRepository.saveAll(archives);

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

}
