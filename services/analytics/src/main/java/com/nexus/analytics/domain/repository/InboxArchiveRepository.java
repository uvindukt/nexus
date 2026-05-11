package com.nexus.analytics.domain.repository;

import com.nexus.analytics.domain.model.InboxArchive;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

@NoRepositoryBean
public interface InboxArchiveRepository extends ReactiveCrudRepository<InboxArchive, UUID> {
    Flux<InboxArchive> findByStatusAndCreatedAtBefore(String status, Instant threshold);
}
