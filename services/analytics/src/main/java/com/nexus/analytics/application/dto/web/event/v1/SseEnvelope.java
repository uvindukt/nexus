package com.nexus.analytics.application.dto.web.event.v1;

import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;

public record SseEnvelope(
        String id,
        String aggregateType,
        String aggregateId,
        String type,
        ProductStockViewResponse payload
) {
}
