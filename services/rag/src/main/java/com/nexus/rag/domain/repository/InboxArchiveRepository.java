package com.nexus.rag.domain.repository;

import com.nexus.rag.domain.model.consumer.InboxArchive;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface InboxArchiveRepository extends ListCrudRepository<InboxArchive, UUID> {
    List<InboxArchive> findByStatusAndCreatedAtBefore(String status, Instant threshold);
}
