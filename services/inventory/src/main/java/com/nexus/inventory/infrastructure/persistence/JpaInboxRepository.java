package com.nexus.inventory.infrastructure.persistence;

import com.nexus.inventory.domain.model.Inbox;
import com.nexus.shared.inbox.InboxStatus;
import com.nexus.inventory.domain.repository.InboxRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaInboxRepository extends JpaRepository<Inbox, UUID>, InboxRepository {

    List<Inbox> findByStatus(InboxStatus status, Pageable pageable);

}
