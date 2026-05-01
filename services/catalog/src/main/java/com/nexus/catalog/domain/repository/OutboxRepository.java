package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.OutboxStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * Port interface for Outbox persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface OutboxRepository {

    Outbox save(Outbox outbox);

    List<Outbox> findByStatusAndCreatedAtBefore(
            OutboxStatus status,
            Instant threshold,
            Pageable pageable
    );

    List<Outbox> findByStatus(OutboxStatus status);

    void deleteAll(List<Outbox> candidates);
}
