package com.nexus.catalog.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.catalog.application.mapper.messaging.OutboxMapper;
import com.nexus.catalog.application.mapper.messaging.ProductEventMapper;
import com.nexus.catalog.domain.exception.OutboxPublishException;
import com.nexus.catalog.domain.model.*;
import com.nexus.catalog.domain.port.out.OutboxPublisherPort;
import com.nexus.catalog.domain.repository.OutboxArchiveRepository;
import com.nexus.catalog.domain.repository.OutboxRepository;
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
    private final OutboxPublisherPort outboxPublisherPort;
    private final OutboxMapper outboxMapper;
    private final ProductEventMapper productEventMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Outbox save(Product product) {

        String payload;

        try {
            payload = objectMapper.writeValueAsString(productEventMapper.toEvent(product));
        } catch (JsonProcessingException e) {
            throw new OutboxPublishException(product.getId(), e);
        }

        Outbox outbox = Outbox.builder()
                .type(OutboxEventType.PRODUCT_CREATED)
                .aggregateType(Product.class.getName())
                .aggregateId(String.valueOf(product.getId()))
                .payload(payload)
                .build();

        outbox = outboxRepository.save(outbox);


        return outbox;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void publishSingle(Outbox outbox) {

        outboxPublisherPort.publishOutbox(outboxMapper.toEvent(outbox), String.valueOf(outbox.getId()));
        outbox.setStatus(OutboxStatus.PROCESSED);
        outbox.setProcessedAt(Instant.now());

    }

    @Transactional
    @Override
    public Integer archive(Instant threshold, Integer limit) {

        List<Outbox> candidateRecords = outboxRepository.findByStatusAndCreatedAtBefore(OutboxStatus.PROCESSED, threshold, PageRequest.of(0, limit));

        if (candidateRecords.isEmpty()) {
            return 0;
        }

        List<OutboxArchive> archives = candidateRecords.stream()
                .map(outboxMapper::toArchive)
                .toList();

        outboxArchiveRepository.saveAll(archives);
        outboxRepository.deleteAll(candidateRecords);

        return candidateRecords.size();

    }

}
