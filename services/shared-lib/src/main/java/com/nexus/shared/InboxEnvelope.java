package com.nexus.shared;

public record InboxEnvelope(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        String payload
) {
}
