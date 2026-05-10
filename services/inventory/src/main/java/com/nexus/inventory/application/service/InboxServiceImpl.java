package com.nexus.inventory.application.service;

import com.nexus.inventory.application.dto.web.request.v1.StockRequest;
import com.nexus.inventory.application.mapper.messaging.InboxMapper;
import com.nexus.inventory.domain.model.Inbox;
import com.nexus.inventory.domain.model.ProductEventType;
import com.nexus.inventory.domain.repository.InboxArchiveRepository;
import com.nexus.inventory.domain.repository.InboxRepository;
import com.nexus.shared.inbox.InboxEnvelope;
import com.nexus.shared.inbox.InboxStatus;
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

    private final StockService stockService;
    private final InboxMapper inboxMapper;
    private final InboxRepository inboxRepository;
    private final InboxArchiveRepository inboxArchiveRepository;

    @Transactional
    @Override
    public void onProductEvent(InboxEnvelope inboxEnvelope) {

        if (idempotencyCheck(inboxEnvelope)) {
            return;
        }

        if (ProductEventType.valueOf(inboxEnvelope.type()) == ProductEventType.PRODUCT_CREATED) {
            stockService.upsertStock(Long.valueOf(inboxEnvelope.aggregateId()), new StockRequest(Long.valueOf(inboxEnvelope.aggregateId()), 0));
        } else {
            log.info("Ignored unknown event Type - {}, ID - {}", inboxEnvelope.type(), inboxEnvelope.id());
        }

        // Saving to Inbox for future idempotency checks
        Inbox inbox = Inbox.builder()
                .id(UUID.fromString(inboxEnvelope.id()))
                .type(inboxEnvelope.type())
                .processedAt(Instant.now())
                .lastAttemptedAt(Instant.now())
                .status(InboxStatus.PROCESSED)
                .build();
        inboxMapper.toInbox(inboxEnvelope, inbox);
        inboxRepository.save(inbox);

    }

    @Transactional
    @Override
    public Integer archive(Integer batchSize) {

        List<Inbox> candidateRecords = inboxRepository.findByStatus(InboxStatus.PROCESSED, PageRequest.of(0, batchSize));
        var archives = inboxMapper.toArchives(candidateRecords);

        if (archives.isEmpty()) {
            return 0;
        }

        inboxArchiveRepository.saveAll(archives);
        inboxRepository.deleteAll(candidateRecords);

        return candidateRecords.size();

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
