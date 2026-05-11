package com.nexus.shared.jdbc;

import com.nexus.shared.common.InboxEventType;
import com.nexus.shared.common.InboxStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
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
    protected String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    protected String payload;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected InboxStatus status = InboxStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    protected Instant createdAt;

    protected Instant processedAt;

    public void setType(InboxEventType inboxEventType) {
        this.type = inboxEventType.name();
    }

}
