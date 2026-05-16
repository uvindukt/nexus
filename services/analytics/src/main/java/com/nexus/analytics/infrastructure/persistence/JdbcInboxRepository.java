package com.nexus.analytics.infrastructure.persistence;

import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.repository.InboxRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JdbcInboxRepository extends JpaRepository<Inbox, UUID>, InboxRepository {

}
