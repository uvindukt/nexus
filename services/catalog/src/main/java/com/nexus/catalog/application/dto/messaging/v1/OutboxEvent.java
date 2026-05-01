package com.nexus.catalog.application.dto.messaging.v1;

public record OutboxEvent(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        String payload
) {
}
