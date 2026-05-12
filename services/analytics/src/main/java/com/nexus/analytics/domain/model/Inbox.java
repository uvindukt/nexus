package com.nexus.analytics.domain.model;

import com.nexus.shared.reactive.AbstractInboxReactive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NullMarked
@Table("inbox")
@Getter
@Setter
@NoArgsConstructor
public class Inbox extends AbstractInboxReactive {

    public static Inbox of(UUID id, String aggregateType, String aggregateId, String type, String payload) {
        return create(Inbox::new, id, aggregateType, aggregateId, type, payload);
    }

}
