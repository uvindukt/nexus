package com.nexus.rag.infrastructure.persistence;

import com.nexus.rag.domain.model.consumer.Inbox;
import com.nexus.rag.domain.repository.InboxRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JdbcInboxRepository extends JpaRepository<Inbox, UUID>, InboxRepository {
}
