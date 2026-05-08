package com.nexus.inventory.domain.model;

import com.nexus.shared.AbstractInbox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@SuperBuilder
@Getter
@Setter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class InboxArchive extends AbstractInbox {

    @CreationTimestamp
    @Column(updatable = false)
    private Instant archivedAt;

}
