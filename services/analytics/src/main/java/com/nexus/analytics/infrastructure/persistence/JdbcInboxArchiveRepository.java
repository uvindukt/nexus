package com.nexus.analytics.infrastructure.persistence;

import com.nexus.analytics.domain.model.InboxArchive;
import com.nexus.analytics.domain.repository.InboxArchiveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JdbcInboxArchiveRepository extends JpaRepository<InboxArchive, UUID>, InboxArchiveRepository {
}
