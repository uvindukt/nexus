package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.OutboxArchive;
import com.nexus.catalog.domain.repository.OutboxArchiveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOutboxArchiveRepository extends JpaRepository<OutboxArchive, UUID>, OutboxArchiveRepository {

}
