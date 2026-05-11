package com.nexus.analytics.domain.model;

import com.nexus.shared.reactive.AbstractInboxReactive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("inbox_archive")
@Getter
@Setter
@NoArgsConstructor
public class InboxArchive extends AbstractInboxReactive {

    public static InboxArchive of(UUID id, String aggregateType, String aggregateId, String type, String payload) {
        return create(InboxArchive::new, id, aggregateType, aggregateId, type, payload);
    }

}
