package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.OutboxArchive;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface OutboxArchiveRepository extends ListCrudRepository<OutboxArchive, UUID> {
}
