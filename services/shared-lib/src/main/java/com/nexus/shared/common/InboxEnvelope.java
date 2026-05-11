package com.nexus.shared.common;

public record InboxEnvelope(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        String payload
) {
}
