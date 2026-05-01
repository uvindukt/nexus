package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.OutboxArchive;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

/**
 * Port interface for OutboxArchive persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface OutboxArchiveRepository extends ListCrudRepository<OutboxArchive, UUID> {

}
