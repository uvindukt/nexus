package com.nexus.catalog.domain.model;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractOutbox {

    @Id
    protected UUID id = UuidCreator.getTimeOrderedEpoch(); // UUIDv7 for better indexing performance

    @Column(nullable = false)
    protected String aggregateType;

    @Column(nullable = false)
    protected String aggregateId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected OutboxEventType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    protected String payload;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected OutboxStatus status = OutboxStatus.PENDING;

    @Column(updatable = false)
    protected Instant createdAt = Instant.now();

    protected Instant processedAt;

}
