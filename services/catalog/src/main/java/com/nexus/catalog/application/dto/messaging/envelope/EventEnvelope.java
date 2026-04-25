package com.nexus.catalog.application.dto.messaging.envelope;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EventEnvelope<T>(
        UUID eventId,
        String eventType,
        String source,
        T data
) {
}
