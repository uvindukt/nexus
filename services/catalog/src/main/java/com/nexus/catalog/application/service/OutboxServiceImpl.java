package com.nexus.catalog.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.catalog.application.mapper.messaging.OutboxEnvelopeMapper;
import com.nexus.catalog.application.mapper.messaging.ProductPayloadMapper;
import com.nexus.catalog.domain.exception.OutboxPublishException;
import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.OutboxArchive;
import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.port.out.ProductPublisher;
import com.nexus.catalog.domain.repository.OutboxArchiveRepository;
import com.nexus.catalog.domain.repository.OutboxRepository;
import com.nexus.shared.common.OutboxEventType;
import com.nexus.shared.common.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxArchiveRepository outboxArchiveRepository;
    private final OutboxRepository outboxRepository;
    private final ProductPublisher productPublisher;
    private final OutboxEnvelopeMapper outboxEnvelopeMapper;
    private final ProductPayloadMapper productPayloadMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void productEvent(Product product, OutboxEventType outboxEventType) {

        String payload;

        try {
            payload = objectMapper.writeValueAsString(productPayloadMapper.toPayload(product));
        } catch (JsonProcessingException e) {
            throw new OutboxPublishException(e, product.getId());
        }

        Outbox outbox = Outbox.builder()
                .type(outboxEventType.name())
                .aggregateType(Product.class.getSimpleName())
                .aggregateId(String.valueOf(product.getId()))
                .payload(payload)
                .build();

        outboxRepository.save(outbox);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void publishSingle(Outbox outbox) {

        productPublisher.publishProduct(outboxEnvelopeMapper.toEnvelope(outbox), String.valueOf(outbox.getId()));
        outbox.setStatus(OutboxStatus.PROCESSED);
        outbox.setProcessedAt(Instant.now());

        outboxRepository.save(outbox); // Detached entity, hence the explicit save

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markFailedAttempt(Outbox outbox) {

        outbox.setRetryCount(outbox.getRetryCount() + 1);
        outbox.setLastAttemptedAt(Instant.now());
        outboxRepository.save(outbox);

    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markFailedEvent(Outbox outbox) {

        outbox.setRetryCount(outbox.getRetryCount() + 1);
        outbox.setLastAttemptedAt(Instant.now());
        outbox.setStatus(OutboxStatus.FAILED);
        outboxRepository.save(outbox);

    }

    @Transactional
    @Override
    public Integer archive(Instant threshold, Integer limit) {

        List<Outbox> candidateRecords = outboxRepository.findByStatusAndCreatedAtBefore(OutboxStatus.PROCESSED, threshold, PageRequest.of(0, limit));

        if (candidateRecords.isEmpty()) {
            return 0;
        }

        List<OutboxArchive> archives = candidateRecords.stream()
                .map(outboxEnvelopeMapper::toArchive)
                .toList();

        outboxArchiveRepository.saveAll(archives);
        outboxRepository.deleteAll(candidateRecords);

        return candidateRecords.size();

    }

}
