package com.nexus.rag.domain.repository;

import com.nexus.rag.domain.model.consumer.Inbox;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface InboxRepository extends ListCrudRepository<Inbox, UUID> {

    @NullMarked
    boolean existsById(UUID id);

}