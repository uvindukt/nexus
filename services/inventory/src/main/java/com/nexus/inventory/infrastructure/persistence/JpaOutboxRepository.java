package com.nexus.inventory.infrastructure.persistence;

import com.nexus.inventory.domain.model.Outbox;
import com.nexus.inventory.domain.repository.OutboxRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOutboxRepository extends JpaRepository<Outbox, UUID>, OutboxRepository {
}
