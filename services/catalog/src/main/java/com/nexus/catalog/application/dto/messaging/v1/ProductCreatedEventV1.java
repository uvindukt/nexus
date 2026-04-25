package com.nexus.catalog.application.dto.messaging.v1;

import com.nexus.catalog.application.dto.messaging.ProductCreatedEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductCreatedEventV1(
        Long productId,
        String code,
        String name,
        String slug,
        BigDecimal price,
        String status,
        Long brandId,
        Long categoryId,
        List<AttributeView> attributes,
        Instant occurredAt
) implements ProductCreatedEvent {

    public record AttributeView(String key, String value) {
    }

}
