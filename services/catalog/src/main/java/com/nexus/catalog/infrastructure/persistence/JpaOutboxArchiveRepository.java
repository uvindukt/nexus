package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.OutboxArchive;
import com.nexus.catalog.domain.repository.OutboxArchiveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface JpaOutboxArchiveRepository extends JpaRepository<OutboxArchive, UUID>, OutboxArchiveRepository {

    String ARCHIVE_QUERY = "INSERT INTO outbox_archive (id, aggregate_type, aggregate_id, type, payload, status, created_at, processed_at) " +
            "SELECT o.id, o.aggregate_type, o.aggregate_id, o.type, o.payload, o.status, o.created_at, o.processed_at " +
            "FROM outbox o " +
            "WHERE o.created_at < :threshold " +
            "LIMIT :limit";

    @Override
    @Modifying
    @Transactional
    @Query(value = ARCHIVE_QUERY, nativeQuery = true)
    Integer archiveOutboxEntries(@Param("threshold") Instant threshold, @Param("limit") Integer limit);

}
