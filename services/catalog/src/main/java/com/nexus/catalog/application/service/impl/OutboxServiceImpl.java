package com.nexus.catalog.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.catalog.application.mapper.messaging.OutboxMapper;
import com.nexus.catalog.application.service.OutboxService;
import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.OutboxEventType;
import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.port.OutboxPublisherPort;
import com.nexus.catalog.domain.repository.OutboxArchiveRepository;
import com.nexus.catalog.domain.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxArchiveRepository outboxArchiveRepository;
    private final OutboxRepository outboxRepository;
    private final OutboxPublisherPort outboxPublisherPort;
    private final OutboxMapper outboxMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Outbox publish(Product product) throws JsonProcessingException {

        Outbox outbox = Outbox.builder()
                .type(OutboxEventType.PRODUCT_CREATED)
                .aggregateType(Product.class.getName())
                .aggregateId(String.valueOf(product.getId()))
                .payload(objectMapper.writeValueAsString(product))
                .build();

        outbox = outboxRepository.save(outbox);
        outboxPublisherPort.publishOutbox(outboxMapper.toOutboxEvent(outbox), String.valueOf(outbox.getId()));

        return outbox;


    }

    @Override
    public Integer archive(Instant threshold, Integer limit) {
        return 0;
    }


}
