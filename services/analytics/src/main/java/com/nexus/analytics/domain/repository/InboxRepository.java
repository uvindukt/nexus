package com.nexus.analytics.domain.repository;

import com.nexus.analytics.domain.model.Inbox;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@NoRepositoryBean
public interface InboxRepository extends ReactiveCrudRepository<Inbox, UUID> {

    @NullMarked
    Mono<Boolean> existsById(UUID id);

}