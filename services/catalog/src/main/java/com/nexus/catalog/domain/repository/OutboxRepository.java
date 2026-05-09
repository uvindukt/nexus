package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Outbox;
import com.nexus.shared.outbox.OutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Port interface for Outbox persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
@NoRepositoryBean
public interface OutboxRepository extends ListCrudRepository<Outbox, UUID> {

    List<Outbox> findByStatusAndCreatedAtBefore(
            OutboxStatus status,
            Instant threshold,
            Pageable pageable
    );

    List<Outbox> findByStatusAndRetryCountLessThan(OutboxStatus status, Integer maxRetries, Pageable pageable);

}
