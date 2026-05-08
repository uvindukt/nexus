package com.nexus.catalog.domain.model;

import com.nexus.shared.AbstractOutbox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends AbstractOutbox {

    @Builder.Default
    @Column(nullable = false)
    protected Integer retryCount = 0;

    @Column(insertable = false)
    protected Instant lastAttemptedAt;

}
