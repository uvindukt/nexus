package com.nexus.inventory.domain.model;

import com.nexus.shared.inbox.AbstractInbox;
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
public class Inbox extends AbstractInbox {

    @Builder.Default
    @Column(nullable = false)
    protected Integer retryCount = 0;

    protected Instant lastAttemptedAt;

}
