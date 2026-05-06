package com.nexus.catalog.domain.model;

public record OutboxEnvelope(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        String payload
) {
}
