package com.nexus.catalog.domain.model;

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
public class OutboxArchive extends AbstractOutbox {

    @CreationTimestamp
    @Column(updatable = false)
    private Instant archivedAt;

}
