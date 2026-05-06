package com.nexus.inventory.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AbstractInbox {

    @Id
    protected UUID id;

    @Column(nullable = false)
    protected String aggregateType;

    @Column(nullable = false)
    protected String aggregateId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected InboxEventType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    protected String payload;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected InboxStatus status = InboxStatus.PENDING;

    @Builder.Default
    @Column(updatable = false)
    protected Instant createdAt = Instant.now();

    protected Instant processedAt;

}
