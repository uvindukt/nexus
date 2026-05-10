package com.nexus.inventory.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.inventory.application.mapper.messaging.StockEventMapper;
import com.nexus.inventory.domain.exception.OutboxPersistException;
import com.nexus.inventory.domain.model.Outbox;
import com.nexus.inventory.domain.model.Stock;
import com.nexus.inventory.domain.repository.OutboxRepository;
import com.nexus.shared.outbox.OutboxEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final StockEventMapper stockEventMapper;

    @Override
    public void stockEvent(Stock stock, OutboxEventType eventType) {

        String payload;

        try {
            payload = objectMapper.writeValueAsString(stockEventMapper.toEvent(stock));
        } catch (JsonProcessingException e) {
            throw new OutboxPersistException(stock.getProductId(), e);
        }

        Outbox outbox = Outbox.builder()
                .type(eventType.name())
                .aggregateType(Stock.class.getSimpleName())
                .aggregateId(String.valueOf(stock.getProductId()))
                .payload(payload)
                .build();

        outboxRepository.save(outbox);

    }

}
