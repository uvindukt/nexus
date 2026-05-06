package com.nexus.inventory.infrastructure.persistence;

import com.nexus.inventory.domain.model.InboxArchive;
import com.nexus.inventory.domain.repository.InboxArchiveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaInboxArchiveRepository extends JpaRepository<InboxArchive, UUID>, InboxArchiveRepository {
}
