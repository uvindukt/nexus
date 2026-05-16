package com.nexus.analytics.domain.model;

import com.nexus.shared.jdbc.AbstractInbox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;

@NullMarked
@SuperBuilder
@Entity
@Getter
@Setter
@NoArgsConstructor
public class InboxArchive extends AbstractInbox {

    @CreationTimestamp
    @Column(updatable = false)
    private Instant archivedAt;

}
