package com.nexus.inventory.infrastructure.persistence;

import com.nexus.inventory.domain.model.OutboxArchive;
import com.nexus.inventory.domain.repository.OutboxArchiveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOutboxArchiveRepository extends JpaRepository<OutboxArchive, UUID>, OutboxArchiveRepository {
}
