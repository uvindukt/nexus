package com.nexus.inventory.domain.model;

public record InboxEnvelope(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        String payload
) {
}
