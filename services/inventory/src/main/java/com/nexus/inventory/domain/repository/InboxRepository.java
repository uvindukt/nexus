package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.Inbox;
import com.nexus.shared.inbox.InboxStatus;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface InboxRepository extends ListCrudRepository<Inbox, UUID> {

    @NullMarked
    boolean existsById(UUID id);

    List<Inbox> findByStatus(InboxStatus status, Pageable pageable);

}
