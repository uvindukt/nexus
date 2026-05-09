package com.nexus.inventory.domain.model;

import com.nexus.shared.outbox.AbstractOutbox;
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
    private Integer retryCount = 0;

    @Column(insertable = false)
    private Instant lastAttemptedAt;

}
