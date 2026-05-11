package com.nexus.analytics.infrastructure.persistence;

import com.nexus.analytics.domain.model.InboxArchive;
import com.nexus.analytics.domain.repository.InboxArchiveRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface R2dbcInboxArchiveRepository extends R2dbcRepository<InboxArchive, UUID>, InboxArchiveRepository {
}
