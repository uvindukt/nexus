package com.nexus.catalog.domain.model;

import com.nexus.shared.outbox.OutboxEventType;

public enum ProductEventType implements OutboxEventType {
    PRODUCT_CREATED,
    PRODUCT_UPDATED,
    PRODUCT_DELETED,
    PRODUCT_ACTIVATED
}
