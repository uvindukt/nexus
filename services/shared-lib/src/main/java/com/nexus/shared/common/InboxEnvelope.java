package com.nexus.shared.common;

import com.fasterxml.jackson.annotation.JsonAlias;

public record InboxEnvelope(
        String id,
        @JsonAlias("aggregate_type")
        String aggregateType,
        @JsonAlias("aggregate_id")
        String aggregateId,
        String type,
        String payload
) {
}
