package com.nexus.catalog.application.dto.messaging.publisher.v1;

public record ProductPublisherEvent(
        Long id,
        String sku,
        String slug
) {
}
