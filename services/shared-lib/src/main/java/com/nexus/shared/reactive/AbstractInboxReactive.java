package com.nexus.shared.reactive;

import com.nexus.shared.common.InboxEventType;
import com.nexus.shared.common.InboxStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractInboxReactive {

    @Id
    protected UUID id;

    @Column("aggregate_type")
    protected String aggregateType;

    @Column("aggregate_id")
    protected String aggregateId;

    @Column("type")
    protected String type;

    @Column("payload")
    protected String payload;

    @Column("status")
    protected String status;

    @Column("created_at")
    protected Instant createdAt;

    @Column("processed_at")
    protected Instant processedAt;

    protected static <T extends AbstractInboxReactive> T create(Supplier<T> constructor, UUID id, String aggregateType, String aggregateId, String type, String payload) {
        T instance = constructor.get();
        instance.id = id;
        instance.aggregateType = aggregateType;
        instance.aggregateId = aggregateId;
        instance.type = type;
        instance.payload = payload;
        instance.status = InboxStatus.PENDING.name();
        instance.createdAt = Instant.now();
        return instance;
    }

    public void setType(InboxEventType inboxEventType) {
        this.type = inboxEventType.name();
    }

}
