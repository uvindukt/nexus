package com.nexus.catalog.domain.repository;

import java.time.Instant;

/**
 * Port interface for OutboxArchive persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface OutboxArchiveRepository {

    Integer archiveOutboxEntries(Instant threshold, Integer limit);

}
