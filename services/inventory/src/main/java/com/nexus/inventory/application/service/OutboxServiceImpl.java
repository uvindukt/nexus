package com.nexus.inventory.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.inventory.application.mapper.messaging.OutboxMapper;
import com.nexus.inventory.application.mapper.messaging.StockEventMapper;
import com.nexus.inventory.domain.exception.OutboxPersistException;
import com.nexus.inventory.domain.model.Outbox;
import com.nexus.inventory.domain.model.OutboxArchive;
import com.nexus.inventory.domain.model.Stock;
import com.nexus.inventory.domain.repository.OutboxArchiveRepository;
import com.nexus.inventory.domain.repository.OutboxRepository;
import com.nexus.shared.outbox.OutboxEventType;
import com.nexus.shared.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final OutboxArchiveRepository outboxArchiveRepository;
    private final OutboxMapper outboxMapper;
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
                .type(eventType)
                .aggregateType(Stock.class.getSimpleName())
                .aggregateId(String.valueOf(stock.getProductId()))
                .payload(payload)
                .build();

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
                .map(outboxMapper::toArchive)
                .toList();

        outboxArchiveRepository.saveAll(archives);
        outboxRepository.deleteAll(candidateRecords);

        return candidateRecords.size();

    }

}
