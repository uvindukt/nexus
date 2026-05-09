package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.repository.OutboxRepository;
import com.nexus.shared.outbox.OutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaOutboxRepository extends JpaRepository<Outbox, UUID>, OutboxRepository {

    List<Outbox> findByStatusAndCreatedAtBefore(
            OutboxStatus status,
            Instant threshold,
            Pageable pageable
    );

    List<Outbox> findByStatusAndRetryCountLessThan(OutboxStatus status, Integer maxRetries, Pageable pageable);

}
