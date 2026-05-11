package com.nexus.shared.common;

public record OutboxEnvelope(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        String payload
) {
}
