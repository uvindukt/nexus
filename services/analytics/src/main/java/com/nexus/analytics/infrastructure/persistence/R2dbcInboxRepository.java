package com.nexus.analytics.infrastructure.persistence;

import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.repository.InboxRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface R2dbcInboxRepository extends R2dbcRepository<Inbox, UUID>, InboxRepository {

}
