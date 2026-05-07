package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.OutboxArchive;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Port interface for OutboxArchive persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
@NoRepositoryBean
public interface OutboxArchiveRepository extends ListCrudRepository<OutboxArchive, UUID> {

}
