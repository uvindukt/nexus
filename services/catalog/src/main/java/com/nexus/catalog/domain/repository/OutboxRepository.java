package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Outbox;

import java.util.UUID;

/**
 * Port interface for Outbox persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface OutboxRepository {

    Outbox save(Outbox outbox);

    void deleteById(UUID id);

}
