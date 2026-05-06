package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.Inbox;
import com.nexus.inventory.domain.model.InboxStatus;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface InboxRepository extends ListCrudRepository<Inbox, UUID> {

    @NullMarked
    boolean existsById(UUID id);

    List<Inbox> findByStatusAndRetryCountLessThan(InboxStatus status, Integer maxRetries, Pageable pageable);

}
