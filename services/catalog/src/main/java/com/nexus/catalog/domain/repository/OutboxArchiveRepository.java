package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.OutboxArchive;

import java.time.Instant;
import java.util.List;

/**
 * Port interface for OutboxArchive persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface OutboxArchiveRepository {

    Integer archiveOutboxEntries(Instant threshold, Integer limit);

    void saveAll(List<OutboxArchive> archives);
}
