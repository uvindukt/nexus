package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.repository.OutboxRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOutboxRepository extends JpaRepository<Outbox, UUID>, OutboxRepository {
}
